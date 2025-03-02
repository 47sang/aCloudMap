mod controller;
mod models;
mod services;
mod task;
mod config;
mod entities;

use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use sea_orm::{Database, DatabaseConnection, ConnectionTrait};
use std::env;
use std::path::Path;
use std::sync::Arc;

use config::create_sql::create_sql;
use controller::index_controller;
use services::db_service::DbService;
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

    // 获取节假日API URL
    // let holiday_url = env::var("HOLIDAY_URL").expect("HOLIDAY_URL must be set");

    // 初始化并启动定时任务
    // let task_service = TaskService::new(db.clone(), holiday_url);
    // tokio::spawn(async move {
    //     if let Err(e) = task_service.start_scheduler().await {
    //         error!("Failed to start scheduler: {}", e);
    //     }
    // });

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(service.clone()))
            .service(web::scope("/info").configure(index_controller::init_routes))
    })
    .bind(format!("0.0.0.0:{}", env::var("PORT").unwrap()))?
    .run()
    .await
}
