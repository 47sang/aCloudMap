use crate::entities::prelude::*;
use crate::entities::a_data_json::{Column, Entity};
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

    pub async fn get_json_only(&self) -> Result<ADataJsonModel, DbErr> {
        Entity::find()
            .order_by_desc(Column::Id)
            .limit(1)
            .one(&*self.db)
            .await?
            .ok_or(DbErr::Custom("没有数据".to_string()))
    }

    pub async fn get_section_bar(&self) -> Result<ADataJsonModel, DbErr> {
        Entity::find()
            .order_by_desc(Column::Id)
            .limit(1)
            .one(&*self.db)
            .await?
            .ok_or(DbErr::Custom("没有数据".to_string()))
    }
}
