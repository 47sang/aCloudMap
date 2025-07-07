mod config;
mod controller;
mod entities;
mod models;
mod services;
mod task;

use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use sea_orm::{ConnectionTrait, Database, DatabaseConnection};
use std::env;
use std::path::Path;
use std::sync::Arc;

use config::create_sql::create_sql;
use controller::index_controller;
use services::{data_service::DataService, db_service::DbService};
use task::task_cron::TaskService;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    // 初始化环境变量和日志
    #[cfg(debug_assertions)] // 开发环境
    dotenv::from_filename(".env.development").ok();

    #[cfg(not(debug_assertions))] // 生产环境
    dotenv::from_filename(".env.production").ok();

    env_logger::init();

    // 数据库连接配置
    let database_url = env::var("DATABASE_URL").expect("没有配置数据库连接");
    // 将相对路径转换为绝对路径
    let database_path = Path::new(&database_url);
    let absolute_path = if database_path.is_relative() {
        std::env::current_dir()?.join(database_path)
    } else {
        database_path.to_path_buf()
    };

    // 构建 SQLite 连接 URL
    let database_url = format!("sqlite:{}?mode=rwc", absolute_path.display());

    // 确保数据库目录存在
    if let Some(parent) = absolute_path.parent() {
        std::fs::create_dir_all(parent)?;
    }

    // 尝试连接数据库，如果数据库文件不存在会自动创建
    let db: DatabaseConnection = Database::connect(&database_url)
        .await
        .expect("无法连接到数据库");

    // 执行创建表的 SQL 语句
    db.execute_unprepared(&create_sql())
        .await
        .expect("创建表失败");

    let db = Arc::new(db);
    let service = DbService::new(db.clone());
    let data_service = DataService::new(db.clone());

    // 获取节假日API URL
    let holiday_url = env::var("HOLIDAY_URL").unwrap_or_else(|_| {
        log::warn!("HOLIDAY_URL 未设置，使用默认值");
        "https://api.example.com/holiday?date=".to_string()
    });

    // 初始化并启动定时任务
    let task_service = TaskService::new(db.clone(), holiday_url);
    let task_service_for_spawn = task_service.clone();
    
    tokio::spawn(async move {
        if let Err(e) = task_service_for_spawn.start_scheduler().await {
            log::error!("Failed to start scheduler: {}", e);
        }
    });

    log::info!("服务器启动中...");

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(service.clone()))
            .app_data(web::Data::new(data_service.clone()))
            .app_data(web::Data::new(task_service.clone()))
            .service(web::scope("/info").configure(index_controller::init_routes))
    })
    .bind(format!("0.0.0.0:{}", env::var("PORT").unwrap_or_else(|_| "8080".to_string())))?
    .run()
    .await
}
