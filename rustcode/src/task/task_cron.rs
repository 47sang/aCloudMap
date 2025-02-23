use chrono::Local;
use log::{error, info};
use serde_json::Value;
use sea_orm::{DatabaseConnection, EntityTrait, ColumnTrait, QueryFilter, ActiveModelTrait, Set};
use std::sync::Arc;
use std::time::Instant;
use tokio_cron_scheduler::{Job, JobScheduler};

use crate::entities::prelude::*;
use crate::entities::holiday::{self, Column};

#[derive(Clone)]
pub struct TaskService {
    db: Arc<DatabaseConnection>,
    holiday_url: String,
}

impl TaskService {
    pub fn new(db: Arc<DatabaseConnection>, holiday_url: String) -> Self {
        Self { db, holiday_url }
    }

    pub async fn start_scheduler(&self) -> Result<(), Box<dyn std::error::Error>> {
        let sched = JobScheduler::new().await?;

        let db = self.db.clone();
        let holiday_url = self.holiday_url.clone();

        // 创建定时任务 "2 0-59/1 9-15 * * 1-5"
        sched
            .add(Job::new_async("2 0-59/1 9-15 * * 1-5", move |_, _| {
                let db = db.clone();
                let holiday_url = holiday_url.clone();

                Box::pin(async move {
                    let task_service = TaskService::new(db, holiday_url);
                    if let Err(e) = task_service.refresh_stock_info().await {
                        error!("刷新股票信息失败: {}", e);
                    }
                })
            })?)
            .await?;

        sched.start().await?;
        Ok(())
    }

    async fn refresh_stock_info(&self) -> Result<(), Box<dyn std::error::Error>> {
        let today = Local::now().format("%Y-%m-%d").to_string();

        // 检查是否为节假日
        if let Some(is_holiday) = self.check_holiday(&today).await? {
            if is_holiday {
                info!("今天是节假日");
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

            // TODO: 实现获取股票信息的逻辑
            // self.get_today_info().await?;

            let duration = start.elapsed();
            info!("定时任务,刷新最新数据耗时:{}秒", duration.as_secs());
        }

        Ok(())
    }

    async fn check_holiday(&self, date: &str) -> Result<Option<bool>, Box<dyn std::error::Error>> {
        let holiday = Holiday::find()
            .filter(Column::Date.eq(date))
            .one(&*self.db)
            .await?;

        if let Some(record) = holiday {
            return Ok(Some(record.holiday));
        }

        let url = format!("{}{}", self.holiday_url, date);
        let response = reqwest::get(&url).await?.json::<Value>().await?;

        let holiday = response.get("holiday");
        let mut is_holiday = false;
        let mut holiday_name = String::new();

        if let Some(holiday_obj) = holiday {
            if let Some(name) = holiday_obj.get("name") {
                holiday_name = name.as_str().unwrap_or("").to_string();
            }
            if let Some(is_hol) = holiday_obj.get("holiday") {
                is_holiday = is_hol.as_bool().unwrap_or(false);
            }
        }

        let holiday = holiday::ActiveModel {
            date: Set(Some(date.to_string())),
            holiday: Set(is_holiday),
            name: Set(Some(holiday_name)),
            ..Default::default()
        };

        holiday.insert(&*self.db).await?;

        Ok(Some(is_holiday))
    }
}
