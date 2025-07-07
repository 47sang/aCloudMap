use std::sync::Arc;
use std::collections::HashMap;

use reqwest::Error;
use sea_orm::{DatabaseConnection, DbErr, EntityTrait, ColumnTrait, QueryFilter, Set, ActiveModelTrait};
use serde_json::{Value, json};
use chrono::{Local, Utc};
use tokio::task::JoinSet;
use anyhow::Result;

use crate::entities::prelude::*;
use crate::entities::{a_today, a_data_json, a_sw_dict, a_sw, a_info};
use crate::models::{AToday, BaseInfo, DataBO, ChildrenDTO, SectionBarVo};
use reqwest::Client;

#[derive(Clone)]
pub struct DataService {
    db: Arc<DatabaseConnection>,
}

impl DataService {
    pub fn new(db: Arc<DatabaseConnection>) -> Self {
        Self { db }
    }

    /// 从东方财富接口获取所有股票信息
    pub async fn get_all_info(&self) -> Result<String, anyhow::Error> {
        // 先尝试从数据库获取今日数据
        if let Ok(data) = self.get_today_data_from_db().await {
            if let Some(json) = data.json {
                return Ok(json);
            }
        }

        // 如果没有今日数据，调用 get_today_info 获取新数据
        self.get_today_info().await
    }

    /// 获取今日股票信息 - 完整实现
    pub async fn get_today_info(&self) -> Result<String, anyhow::Error> {
        // 创建并发请求获取所有页面数据
        let mut join_set: JoinSet<Result<Value, anyhow::Error>> = JoinSet::new();
        let client = Client::new();

        // 并发请求29页数据
        for page in 1..=29 {
            let client = client.clone();
            join_set.spawn(async move {
                let params = [
                    ("pn", page.to_string()),
                    ("pz", "200".to_string()),
                    ("po", "1".to_string()),
                    ("np", "1".to_string()),
                    ("ut", "bd1d9ddb04089700cf9c27f6f7426281".to_string()),
                    ("fltt", "2".to_string()),
                    ("invt", "2".to_string()),
                    ("fid", "f3".to_string()),
                    ("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048".to_string()),
                    ("fields", "f2,f3,f8,f12,f14,f20,f26".to_string()),
                ];

                let response = client
                    .get("https://82.push2.eastmoney.com/api/qt/clist/get")
                    .query(&params)
                    .send()
                    .await?;

                let text = response.text().await?;
                let replaced = text.replace("\"-\"", "0");
                let json: Value = serde_json::from_str(&replaced)?;
                
                if let Some(data) = json.get("data") {
                    if let Some(diff) = data.get("diff") {
                        return Ok(diff.clone());
                    }
                }
                
                Ok(Value::Array(vec![]))
            });
        }

        // 收集所有结果
        let mut merged_array = Vec::new();
        while let Some(result) = join_set.join_next().await {
            match result {
                Ok(Ok(data)) => {
                    if let Value::Array(arr) = data {
                        merged_array.extend(arr);
                    }
                }
                Ok(Err(e)) => log::error!("获取股票数据失败: {}", e),
                Err(e) => log::error!("任务执行失败: {}", e),
            }
        }

        // 更新今日股票数据
        let a_today_list = self.update_today_info(Value::Array(merged_array)).await?;

        // 设置数据映射
        let data_json = self.set_data_to_map(a_today_list).await?;

        // 保存到数据库
        if let Err(e) = self.save_data_json(&data_json).await {
            log::error!("保存数据失败: {}", e);
        }

        Ok(serde_json::to_string(&data_json).unwrap_or_default())
    }

    /// 更新今日股票数据
    async fn update_today_info(&self, json_array: Value) -> Result<Vec<AToday>, anyhow::Error> {
        let today = Local::now().format("%Y-%m-%d").to_string();
        
        // 获取今日已有数据
        let existing_data = a_today::Entity::find()
            .filter(a_today::Column::Today.eq(&today))
            .all(&*self.db)
            .await?;

        let existing_map: HashMap<String, a_today::Model> = existing_data
            .into_iter()
            .filter_map(|item| item.code.clone().map(|code| (code, item)))
            .collect();

        let mut new_list = Vec::new();

        if let Value::Array(array) = json_array {
            for (index, item) in array.iter().enumerate() {
                let base_info: BaseInfo = serde_json::from_value(item.clone())?;
                
                let code = base_info.f12.unwrap_or_default();
                let existing_record = existing_map.get(&code);
                
                let total = base_info.f20.map(|v| (v as i64).to_string());
                let price = base_info.f2.map(|v| v.to_string());
                let increase = base_info.f3.map(|v| v.to_string());
                let turnover = base_info.f8.map(|v| v.to_string());
                
                let arr_value = json!([
                    base_info.f20.unwrap_or(0.0) as i64,
                    base_info.f2.unwrap_or(0.0),
                    base_info.f3.unwrap_or(0.0)
                ]);

                let a_today = AToday {
                    id: existing_record.map(|r| r.id).unwrap_or(index as i32 + 1),
                    code: Some(code),
                    name: base_info.f14,
                    total,
                    price,
                    increase,
                    turnover,
                    into_date: base_info.f26,
                    today: Some(today.clone()),
                    creat_time: Some(Local::now().format("%Y-%m-%d %H:%M:%S").to_string()),
                    arr_value: Some(arr_value.to_string()),
                    value: Some(arr_value.to_string()),
                };

                // 保存到数据库
                let active_model = a_today::ActiveModel {
                    id: if let Some(existing) = existing_record {
                        sea_orm::ActiveValue::Unchanged(existing.id)
                    } else {
                        sea_orm::ActiveValue::NotSet
                    },
                    code: Set(a_today.code.clone()),
                    name: Set(a_today.name.clone()),
                    total: Set(a_today.total.clone()),
                    price: Set(a_today.price.clone()),
                    increase: Set(a_today.increase.clone()),
                    turnover: Set(a_today.turnover.clone()),
                    into_date: Set(a_today.into_date.clone()),
                    today: Set(a_today.today.clone()),
                    creat_time: Set(a_today.creat_time.clone()),
                    arr_value: Set(a_today.arr_value.clone()),
                    ..Default::default()
                };

                if existing_record.is_some() {
                    active_model.update(&*self.db).await?;
                } else {
                    active_model.insert(&*self.db).await?;
                }

                new_list.push(a_today);
            }
        }

        Ok(new_list)
    }

    /// 设置数据映射 - 处理一级分类和二级分类
    async fn set_data_to_map(&self, a_today_list: Vec<AToday>) -> Result<Vec<Value>, anyhow::Error> {
        // 获取一级分类所有code
        let code_list = a_sw_dict::Entity::find()
            .filter(a_sw_dict::Column::Type.like("%一%"))
            .all(&*self.db)
            .await?;

        // 获取一级分类所有股票
        let a_list = a_sw::Entity::find()
            .filter(a_sw::Column::IndustryType.like("%一%"))
            .all(&*self.db)
            .await?;

        let mut json_array = Vec::new();

        for item in code_list {
            // 将一级分类的股票过滤出来
            let collect: Vec<_> = a_list
                .iter()
                .filter(|item1| item1.sw_code.as_ref() == Some(&item.code))
                .collect();

            // 根据一级分类将股票数据过滤出来
            let mut item_list: Vec<_> = a_today_list
                .iter()
                .filter(|item1| {
                    collect
                        .iter()
                        .any(|sw| sw.stock_code.as_ref() == item1.code.as_ref())
                })
                .cloned()
                .collect();

            // 按总市值排序
            item_list.sort_by(|a, b| {
                let a_total = a.total.as_ref().and_then(|s| s.parse::<i64>().ok()).unwrap_or(0);
                let b_total = b.total.as_ref().and_then(|s| s.parse::<i64>().ok()).unwrap_or(0);
                b_total.cmp(&a_total)
            });

            // 处理二级分类
            let ej_list = a_info::Entity::find()
                .filter(a_info::Column::BkId.eq(&item.code))
                .all(&*self.db)
                .await?;

            // 整理出二级分类的代码和股票代码,去重的数据
            let mut distinct_map: HashMap<String, &a_info::Model> = HashMap::new();
            for ej in &ej_list {
                if let Some(ej_id) = &ej.ej_id {
                    distinct_map.insert(ej_id.clone(), ej);
                }
            }

            let mut ej_array = Vec::new();
            for (_, ej) in distinct_map {
                // 根据二级分类将股票数据过滤出来
                let ej_codes: Vec<&String> = ej_list
                    .iter()
                    .filter(|ej_item| ej_item.ej_id == ej.ej_id)
                    .map(|info| &info.code)
                    .collect();

                let ej_today_list: Vec<_> = item_list
                    .iter()
                    .filter(|a_today| {
                        ej_codes.contains(&a_today.code.as_ref().unwrap_or(&String::new()))
                    })
                    .collect();

                // 计算二级分类总市值
                let ej_total: i64 = ej_today_list
                    .iter()
                    .map(|item| item.total.as_ref().and_then(|s| s.parse::<i64>().ok()).unwrap_or(0))
                    .sum();

                let value2 = json!([ej_total, null, null]);

                let ej_json = json!({
                    "name": ej.ej_name,
                    "code": ej.ej_id,
                    "total": ej_total,
                    "value": value2,
                    "children": ej_today_list
                });

                ej_array.push(ej_json);
            }

            // 按总市值排序二级分类
            ej_array.sort_by(|a, b| {
                let a_total = a.get("total").and_then(|v| v.as_i64()).unwrap_or(0);
                let b_total = b.get("total").and_then(|v| v.as_i64()).unwrap_or(0);
                b_total.cmp(&a_total)
            });

            // 计算一级分类总市值
            let total: i64 = item_list
                .iter()
                .map(|item| item.total.as_ref().and_then(|s| s.parse::<i64>().ok()).unwrap_or(0))
                .sum();
            let value = json!([total, null, null]);

            let json_object = json!({
                "name": item.name,
                "code": item.code,
                "total": total,
                "value": value,
                "children": ej_array
            });

            json_array.push(json_object);
        }

        // 根据板块总市值进行从大到小排序
        json_array.sort_by(|a, b| {
            let a_total = a.get("total").and_then(|v| v.as_i64()).unwrap_or(0);
            let b_total = b.get("total").and_then(|v| v.as_i64()).unwrap_or(0);
            b_total.cmp(&a_total)
        });

        Ok(json_array)
    }

    /// 保存数据到数据库
    async fn save_data_json(&self, data: &[Value]) -> Result<(), anyhow::Error> {
        let today = Local::now().format("%Y-%m-%d").to_string();
        
        // 删除今日旧数据
        a_data_json::Entity::delete_many()
            .filter(a_data_json::Column::Today.eq(&today))
            .exec(&*self.db)
            .await?;

        // 保存新数据
        let json_data = serde_json::to_string(data)?;
        let section_data = serde_json::to_string(data)?;

        let active_model = a_data_json::ActiveModel {
            json: Set(Some(json_data)),
            section: Set(Some(section_data)),
            today: Set(Some(today)),
            creat_time: Set(Some(Local::now().naive_local().format("%Y-%m-%d %H:%M:%S").to_string())),
            ..Default::default()
        };

        active_model.insert(&*self.db).await?;
        Ok(())
    }

    /// 从数据库获取今日数据
    async fn get_today_data_from_db(&self) -> Result<crate::entities::a_data_json::Model, DbErr> {
        a_data_json::Entity::find()
            .filter(a_data_json::Column::Today.eq(Local::now().format("%Y-%m-%d").to_string()))
            .one(&*self.db)
            .await?
            .ok_or(DbErr::Custom("没有找到今日数据".to_string()))
    }

    /// 获取二级板块条形图数据
    pub async fn get_section_bar(&self) -> Result<SectionBarVo, anyhow::Error> {
        let today_data = self.get_today_data_from_db().await
            .map_err(|e| anyhow::anyhow!("获取数据失败: {}", e))?;

        let section_data = today_data.section
            .ok_or_else(|| anyhow::anyhow!("没有板块数据"))?;

        let section_bo: Vec<DataBO> = serde_json::from_str(&section_data)?;

        let mut sort_list = Vec::new();
        for item in section_bo {
            sort_list.extend(item.children);
        }

        // 按照涨跌额排序
        sort_list.sort_by(|a, b| b.turnover.partial_cmp(&a.turnover).unwrap_or(std::cmp::Ordering::Equal));

        // 添加序号
        for (i, item) in sort_list.iter_mut().enumerate() {
            item.name = format!("{}. {}", i + 1, item.name);
        }

        // 按照总市值排序
        sort_list.sort_by(|a, b| a.total.partial_cmp(&b.total).unwrap_or(std::cmp::Ordering::Equal));

        let mut category_name = Vec::new();
        let mut now_num = Vec::new();
        let mut yesterday_num = Vec::new();
        let mut change_amount = Vec::new();

        for item in sort_list {
            category_name.push(format!("{}({:.2}%)", item.name, item.increase));
            now_num.push((item.total / 100_000_000.0 * 100.0).round() / 100.0);
            yesterday_num.push(((item.total - item.turnover) / 100_000_000.0 * 100.0).round() / 100.0);
            change_amount.push((item.turnover / 100_000_000.0 * 100.0).round() / 100.0);
        }

        Ok(SectionBarVo {
            category_name,
            now_num,
            yesterday_num,
            change_amount,
        })
    }
}
