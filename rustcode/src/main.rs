mod controller;
mod models;
mod services;
mod task;
mod config;

use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use log::{error, info};
use sqlx::SqlitePool;
use std::env;
use std::sync::Arc;

use config::create_sql::create_sql;
use controller::stock_handler;
use services::db_service::DbService;
use task::TaskService;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    // 初始化环境变量和日志
    #[cfg(debug_assertions)] // 开发环境
    dotenv::from_filename(".env.development").ok();

    #[cfg(not(debug_assertions))] // 生产环境
    dotenv::from_filename(".env.production").ok();

    env_logger::init();

    // 数据库连接（这里使用示例URL，实际使用时需要从环境变量获取）
    let database_url = env::var("DATABASE_URL").expect("没有配置数据库连接");

    // 尝试连接数据库，如果数据库文件不存在，则创建它
    if !std::path::Path::new(&database_url).exists() {
        // 创建数据库文件
        match std::fs::File::create(&database_url) {
            Ok(_) => info!("数据库文件创建成功"),
            Err(e) => panic!("创建数据库文件失败: {}", e),
        }

        info!("表创建成功");
    }

    let pool = SqlitePool::connect(&database_url)
        .await
        .expect("无法连接到数据库");
    // 从文件中读取 SQL 语句
    // let create_table_sql = include_str!("../create_tables.sql");

    // 在这里执行创建表的 SQL 语句
    sqlx::query(&create_sql())
        .execute(&pool)
        .await
        .expect("创建表失败");

    let pool = Arc::new(pool);
    let service = DbService::new(pool);
    // 获取节假日API URL
    // let holiday_url = env::var("HOLIDAY_URL").expect("HOLIDAY_URL must be set");

    // 初始化并启动定时任务
    // let task_service = TaskService::new(pool.clone(), holiday_url);
    // tokio::spawn(async move {
    //     if let Err(e) = task_service.start_scheduler().await {
    //         error!("Failed to start scheduler: {}", e);
    //     }
    // });

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(service.clone()))
            .service(web::scope("/info").configure(stock_handler::init_routes))
    })
    .bind(format!("0.0.0.0:{}", env::var("PORT").unwrap()))?
    .run()
    .await
}
