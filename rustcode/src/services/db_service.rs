use crate::models::{ADataJson, AInfo, ASwDict, AToday, ApiResponse};
use sqlx::{Error, SqlitePool};
use std::sync::Arc;

#[derive(Clone)]
pub struct DbService {
    db: Arc<SqlitePool>,
}

impl DbService {
    pub fn new(db: Arc<SqlitePool>) -> Self {
        Self { db }
    }

    pub async fn get_json_only(&self) -> Result<ADataJson, Error> {
        let rows =
            sqlx::query_as::<_, ADataJson>("SELECT * FROM a_data_json ORDER BY id DESC LIMIT 1")
                .fetch_one(&*self.db)
                .await?;
        Ok(rows)
    }

    pub async fn get_section_bar(&self) -> Result<ADataJson, Error> {
        let rows =
            sqlx::query_as::<_, ADataJson>("SELECT * FROM a_data_json ORDER BY id DESC LIMIT 1")
                .fetch_one(&*self.db)
                .await?;

        Ok(rows)
    }
}
