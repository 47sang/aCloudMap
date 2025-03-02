use crate::entities::prelude::*;
use crate::entities::{a_data_json::Column, a_data_json::Entity,a_data_json};
use sea_orm::{DatabaseConnection, EntityTrait, DbErr, QueryOrder, QuerySelect};
use std::sync::Arc;

#[derive(Clone)]
pub struct DbService {
    db: Arc<DatabaseConnection>,
}

impl DbService {
    pub fn new(db: Arc<DatabaseConnection>) -> Self {
        Self { db }
    }

    /// 获取最新的一条今日的A股json数据
    pub async fn get_json_only(&self) -> Result<a_data_json::Model, DbErr> {
        Entity::find()
            .order_by_desc(Column::Id)
            .one(&*self.db)
            .await?
            .ok_or(DbErr::Custom("没有数据".to_string()))
    }
}
