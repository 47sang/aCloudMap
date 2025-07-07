use chrono::Local;
use log::{error, info};
use serde_json::Value;
use sea_orm::{DatabaseConnection, EntityTrait, ColumnTrait, QueryFilter, ActiveModelTrait, Set};
use std::sync::Arc;
use std::time::Instant;
use tokio_cron_scheduler::{Job, JobScheduler};

use crate::entities::prelude::*;
use crate::entities::a_holiday::{self, Column};
use crate::services::data_service::DataService;

#[derive(Clone)]
pub struct TaskService {
    db: Arc<DatabaseConnection>,
    holiday_url: String,
    data_service: DataService,
}

impl TaskService {
    pub fn new(db: Arc<DatabaseConnection>, holiday_url: String) -> Self {
        let data_service = DataService::new(db.clone());
        Self { 
            db, 
            holiday_url,
            data_service,
        }
    }

    pub async fn start_scheduler(&self) -> Result<(), Box<dyn std::error::Error>> {
        let sched = JobScheduler::new().await?;

        let task_service = self.clone();

        // 创建定时任务 "2 0-59/1 9-15 * * 1-5"
        // 工作日上午开盘期间每分钟第2秒执行
        sched
            .add(Job::new_async("2 0-59/1 9-15 * * 1-5", move |_, _| {
                let task_service = task_service.clone();
                Box::pin(async move {
                    if let Err(e) = task_service.refresh_stock_info().await {
                        error!("刷新股票信息失败: {}", e);
                    }
                })
            })?)
            .await?;

        // 添加下午交易时间的定时任务 "2 0-59/1 13-15 * * 1-5"
        let task_service_afternoon = self.clone();
        sched
            .add(Job::new_async("2 0-59/1 13-15 * * 1-5", move |_, _| {
                let task_service = task_service_afternoon.clone();
                Box::pin(async move {
                    if let Err(e) = task_service.refresh_stock_info().await {
                        error!("刷新股票信息失败: {}", e);
                    }
                })
            })?)
            .await?;

        sched.start().await?;
        info!("定时任务调度器启动成功");
        Ok(())
    }

    async fn refresh_stock_info(&self) -> Result<(), Box<dyn std::error::Error>> {
        let today = Local::now().format("%Y-%m-%d").to_string();

        // 检查是否为节假日
        if let Some(is_holiday) = self.check_holiday(&today).await? {
            if is_holiday {
                info!("今天是节假日，跳过股票数据刷新");
                return Ok(());
            }
        }

        // 检查当前时间是否在交易时间内
        let now = Local::now().time();
        let is_trading_time = (now >= chrono::NaiveTime::from_hms_opt(9, 15, 0).unwrap()
            && now <= chrono::NaiveTime::from_hms_opt(11, 30, 5).unwrap())
            || (now >= chrono::NaiveTime::from_hms_opt(13, 0, 0).unwrap()
                && now <= chrono::NaiveTime::from_hms_opt(15, 0, 5).unwrap());

        if is_trading_time {
            let start = Instant::now();

            // 调用数据服务获取最新股票信息
            match self.data_service.get_today_info().await {
                Ok(_) => {
                    let duration = start.elapsed();
                    info!("定时任务,刷新最新数据耗时:{}秒", duration.as_secs());
                }
                Err(e) => {
                    error!("定时任务刷新股票数据失败: {}", e);
                }
            }
        } else {
            info!("当前不在交易时间内，跳过股票数据刷新");
        }

        Ok(())
    }

    async fn check_holiday(&self, date: &str) -> Result<Option<bool>, Box<dyn std::error::Error>> {
        // 先从数据库查询是否已经缓存了节假日信息
        let holiday = a_holiday::Entity::find()
            .filter(Column::Date.eq(date))
            .one(&*self.db)
            .await?;

        if let Some(record) = holiday {
            return Ok(Some(record.holiday.unwrap_or(0) == 1));
        }

        // 如果数据库中没有，调用节假日API
        let url = format!("{}{}", self.holiday_url, date);
        match reqwest::get(&url).await {
            Ok(response) => {
                match response.json::<Value>().await {
                    Ok(json) => {
                        let mut is_holiday = false;
                        let mut holiday_name = String::new();

                        if let Some(holiday_obj) = json.get("holiday") {
                            if let Some(name) = holiday_obj.get("name") {
                                holiday_name = name.as_str().unwrap_or("").to_string();
                            }
                            if let Some(is_hol) = holiday_obj.get("holiday") {
                                is_holiday = is_hol.as_bool().unwrap_or(false);
                            }
                        }

                        // 保存到数据库
                        let holiday_record = a_holiday::ActiveModel {
                            date: Set(Some(date.to_string())),
                            holiday: Set(Some(is_holiday as i32)),
                            name: Set(Some(holiday_name)),
                            ..Default::default()
                        };

                        if let Err(e) = holiday_record.insert(&*self.db).await {
                            error!("保存节假日信息失败: {}", e);
                        }

                        Ok(Some(is_holiday))
                    }
                    Err(e) => {
                        error!("解析节假日API响应失败: {}", e);
                        // 如果API调用失败，默认不是节假日（让系统正常运行）
                        Ok(Some(false))
                    }
                }
            }
            Err(e) => {
                error!("调用节假日API失败: {}", e);
                // 如果API调用失败，默认不是节假日（让系统正常运行）
                Ok(Some(false))
            }
        }
    }

    /// 手动触发数据刷新（用于测试或手动调用）
    pub async fn manual_refresh(&self) -> Result<(), Box<dyn std::error::Error>> {
        info!("手动触发数据刷新");
        let start = Instant::now();

        match self.data_service.get_today_info().await {
            Ok(_) => {
                let duration = start.elapsed();
                info!("手动刷新数据耗时:{}秒", duration.as_secs());
                Ok(())
            }
            Err(e) => {
                error!("手动刷新股票数据失败: {}", e);
                Err(format!("手动刷新股票数据失败: {}", e).into())
            }
        }
    }
}
