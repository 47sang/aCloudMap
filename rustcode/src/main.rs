mod handlers;
mod models;
mod services;

use actix_cors::Cors;
use actix_web::{web, App, HttpServer};
use dotenv::dotenv;
use sqlx::MySqlPool;
use std::env;
use std::sync::Arc;

use crate::handlers::stock_handler;
use crate::services::stock_service::StockService;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    // 初始化环境变量和日志
    dotenv().ok();
    env_logger::init();

    // 数据库连接（这里使用示例URL，实际使用时需要从环境变量获取）
    let database_url = env::var("DATABASE_URL")
        .unwrap_or_else(|_| "mysql://root:password@localhost/stockdb".to_string());

    let pool = MySqlPool::connect(&database_url)
        .await
        .expect("Failed to connect to database");

    let pool = Arc::new(pool);
    let service = StockService::new(pool);

    println!("Server running at http://localhost:8080");

    HttpServer::new(move || {
        App::new()
            .wrap(Cors::permissive())
            .app_data(web::Data::new(service.clone()))
            .service(
                web::scope("/info")
                    .service(stock_handler::get_all_info)
                    .service(stock_handler::get_sort_info)
                    .service(stock_handler::get_section)
                    .service(stock_handler::get_section_bar)
            )
    })
    .bind("127.0.0.1:8080")?
    .run()
    .await
}
