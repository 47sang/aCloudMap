use crate::services::db_service::StockService;
use actix_web::{get, web, HttpResponse, Responder};

/// 获取一二级分类整理后的所有股票信息
#[get("/all")]
pub async fn get_all_info(service: web::Data<StockService>) -> impl Responder {
    let result = service.get_json_only().await;
    HttpResponse::Ok().json(result)
}

/// 按照市值排序的股票信息
#[get("/sort")]
pub async fn get_sort_info(service: web::Data<StockService>) -> impl Responder {
    let result = service.get_sort_info().await;
    HttpResponse::Ok().json(result)
}

/// 获取板块涨跌信息
#[get("/section")]
pub async fn get_section(service: web::Data<StockService>) -> impl Responder {
    let result = service.get_section().await;
    HttpResponse::Ok().json(result)
}

/// 获取二级板块正负条形图数据
#[get("/sectionBar")]
pub async fn get_section_bar(service: web::Data<StockService>) -> impl Responder {
    let result = service.get_section_bar().await;
    HttpResponse::Ok().json(result)
}

pub fn init_routes(config: &mut web::ServiceConfig) {
    config
        .service(get_all_info)
        .service(get_sort_info)
        .service(get_section)
        .service(get_section_bar)
}
