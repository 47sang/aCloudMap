use crate::models::{ADataJson, AInfo, ASwDict, AToday, ApiResponse};
use sqlx::{SqlitePool, Row};
use std::sync::Arc;
use serde_json::Value;

#[derive(Clone)]
pub struct DbService {
    db: Arc<SqlitePool>,
}

impl DbService {
    pub fn new(db: Arc<SqlitePool>) -> Self {
        Self { db }
    }
    
}
