use std::sync::Arc;

use reqwest::Error;
use sea_orm::{DatabaseConnection, DbErr};

use crate::entities::a_data_json;
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
    pub async fn get_all_info(&self) -> Result<String, Error> {
        // 发起HTTP GET请求获取数据
        let client = Client::new();
        let params = [
            ("pn", "1"),
            ("pz", "200"),
            ("po", "1"),
            ("np", "1"),
            ("ut", "bd1d9ddb04089700cf9c27f6f7426281"),
            ("fltt", "2"),
            ("invt", "2"),
            ("fid", "f3"),
            ("fs", "m:0 t:6,m:0 t:13,m:0 t:80,m:1 t:2,m:1 t:23"),
            ("fields", "f2,f3,f8,f12,f14,f20,f26"),
        ];
        let response = client
            .get("https://82.push2.eastmoney.com/api/qt/clist/get")
            .query(&params)
            .send()
            .await?;

        if response.status().is_success() {
            Ok(response.text().await?)
        }else {
            Err(response.error_for_status().unwrap_err())
        }
    }
}
