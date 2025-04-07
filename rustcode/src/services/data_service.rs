use std::sync::Arc;

use sea_orm::{DatabaseConnection, DbErr};

use crate::entities::a_data_json;



#[derive(Clone)]
pub struct DataService {
    db: Arc<DatabaseConnection>,
}

impl DataService {
    pub fn new(db: Arc<DatabaseConnection>) -> Self {
        Self { db }
    }

    /// 从东方财富接口获取所有股票信息
    pub async fn get_all_info(&self) -> Result<Vec<a_data_json::Model>, DbErr> {
        // 发起HTTP GET请求获取数据
        let client = reqwest::Client::new();
        let params = [("key1", "value1"),
         ("key2", "value2")];
        let response = client
            .get("http://api.example.com/stocks") // 替换为实际的API端点
            .query(&params)
            .send()
            .await?;

        // 检查响应状态
        if !response.status().is_success() {
            return Err(DbErr::Custom(format!("请求失败: {}", response.status())));
        }

        // 解析响应数据
        let data = response
            .json::<Vec<a_data_json::Model>>()
            .await
            .map_err(|e| DbErr::Custom(format!("解析响应数据失败: {}", e)))?;

        Ok(data)
    }
}
