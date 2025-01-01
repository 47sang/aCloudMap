use crate::models::{ADataJson, AInfo, ASwDict, AToday, ApiResponse};
use sqlx::{MySqlPool, Row};
use std::sync::Arc;
use serde_json::Value;

#[derive(Clone)]
pub struct StockService {
    db: Arc<MySqlPool>,
}

impl StockService {
    pub fn new(db: Arc<MySqlPool>) -> Self {
        Self { db }
    }

    pub async fn get_json_only(&self) -> ApiResponse<Option<Value>> {
        match sqlx::query!(
            r#"SELECT CAST(json as CHAR) as json 
             FROM a_data_json ORDER BY id DESC LIMIT 1"#
        )
        .fetch_optional(&*self.db)
        .await
        {
            Ok(result) => match result {
                Some(row) => {
                    match serde_json::from_str(&row.json.unwrap()) {
                        Ok(parsed) => ApiResponse::success(Some(parsed)),
                        Err(e) => ApiResponse::fail(&format!("JSON解析失败: {}", e)),
                    }
                },
                None => ApiResponse::success(None),
            },
            Err(e) => ApiResponse::fail(&format!("获取json数据失败: {}", e)),
        }
    }

    pub async fn get_all_info(&self) -> ApiResponse<Vec<ADataJson>> {
        match sqlx::query_as!(
            ADataJson,
            r#"SELECT id, 
               CAST(json as CHAR) as json,
               CAST(section as CHAR) as section,
               CAST(today as CHAR) as today,
               creat_time 
               FROM a_data_json ORDER BY id DESC LIMIT 1"#
        )
        .fetch_all(&*self.db)
        .await
        {
            Ok(info) => ApiResponse::success(info),
            Err(e) => ApiResponse::fail(&format!("获取数据失败: {}", e)),
        }
    }

    pub async fn get_today_data(&self) -> ApiResponse<Option<ADataJson>> {
        match sqlx::query(
            r#"SELECT id, 
               CAST(json as CHAR) as json,
               CAST(section as CHAR) as section,
               CAST(today as CHAR) as today,
               creat_time
               FROM a_data_json ORDER BY id DESC LIMIT 1"#,
        )
        .fetch_optional(&*self.db)
        .await
        {
            Ok(row) => match row {
                Some(r) => {
                    let id: i32 = r.get("id");
                    let json: Option<String> = r.get("json");
                    let section: Option<String> = r.get("section");
                    let today: Option<String> = r.get("today");
                    let creat_time: Option<chrono::NaiveDateTime> = r.get("creat_time");

                    ApiResponse::success(Some(ADataJson {
                        id,
                        json,
                        section,
                        today,
                        creat_time,
                    }))
                }
                None => ApiResponse::success(None),
            },
            Err(e) => ApiResponse::fail(&format!("获取今日数据失败: {}", e)),
        }
    }

    pub async fn get_sort_info(&self) -> ApiResponse<Vec<AToday>> {
        ApiResponse::success(vec![])
    }

    pub async fn get_section(&self) -> ApiResponse<String> {
        match self.get_today_data().await {
            ApiResponse {
                code: 200,
                data: Some(Some(data)),
                ..
            } => match data.section {
                Some(section) => ApiResponse::success(section),
                None => ApiResponse::fail("板块数据为空"),
            },
            _ => ApiResponse::fail("获取今日数据失败"),
        }
    }

    pub async fn get_section_bar(&self) -> ApiResponse<String> {
        // 复用 get_section 的逻辑，因为它们使用相同的数据源
        self.get_section().await
    }

    pub async fn get_sw_dict_by_type(&self, type_: &str) -> ApiResponse<Vec<ASwDict>> {
        ApiResponse::success(vec![])
    }
}
