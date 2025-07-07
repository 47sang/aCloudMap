use crate::models::{app_error::AppError, AToday, ApiResponse, BaseInfo};
use crate::services::data_service::DataService;
use crate::services::db_service::DbService;
use actix_web::{get, web, HttpResponse, Responder};
use reqwest::Client;
use serde_json::Value;
use std::collections::HashMap;
use std::env;


/// 获取一二级分类整理后的所有股票信息
#[get("/all")]
pub async fn get_all_info(data_service: web::Data<DataService>) -> Result<HttpResponse, AppError> {
    let result = data_service
        .get_all_info()
        .await
        .map_err(|e| AppError::DbError(e.to_string()))?;

    let json: Value =
        serde_json::from_str(&result).map_err(|e| AppError::JsonError(e.to_string()))?;

    Ok(HttpResponse::Ok().json(ApiResponse::success(json)))
}

/// 按照市值排序的股票信息
#[get("/sort")]
pub async fn get_sort_info(data_service: web::Data<DataService>) -> Result<HttpResponse, AppError> {
    // 直接调用已有的 get_today_info 方法获取排序后的数据
    let result = data_service
        .get_today_info()
        .await
        .map_err(|e| AppError::DbError(e.to_string()))?;

    // 解析数据并按市值重新排序
    let json: Value = serde_json::from_str(&result)
        .map_err(|e| AppError::JsonError(e.to_string()))?;

    // 从所有分类中提取股票数据并按市值排序
    let mut all_stocks = Vec::new();
    if let Value::Array(categories) = json {
        for category in categories {
            if let Some(children) = category.get("children") {
                if let Value::Array(sub_categories) = children {
                    for sub_category in sub_categories {
                        if let Some(stocks) = sub_category.get("children") {
                            if let Value::Array(stock_list) = stocks {
                                all_stocks.extend(stock_list.clone());
                            }
                        }
                    }
                }
            }
        }
    }

    // 按总市值排序
    all_stocks.sort_by(|a, b| {
        let a_total = a.get("total").and_then(|v| v.as_i64()).unwrap_or(0);
        let b_total = b.get("total").and_then(|v| v.as_i64()).unwrap_or(0);
        b_total.cmp(&a_total)
    });

    // 添加序号
    for (i, stock) in all_stocks.iter_mut().enumerate() {
        if let Some(obj) = stock.as_object_mut() {
            obj.insert("id".to_string(), Value::Number((i + 1).into()));
        }
    }

    Ok(HttpResponse::Ok().json(ApiResponse::success(all_stocks)))
}

/// 获取板块涨跌信息
#[get("/section")]
pub async fn get_section(db: web::Data<DbService>) -> Result<HttpResponse, AppError> {
    let section = db.get_json_only().await
        .map_err(|e| AppError::DbError(e.to_string()))?;
    
    let json: Value = if let Some(section_data) = section.section {
        serde_json::from_str(&section_data)
            .map_err(|e| AppError::JsonError(e.to_string()))?
    } else {
        return Err(AppError::DbError("没有板块数据".to_string()));
    };
    
    Ok(HttpResponse::Ok().json(ApiResponse::success(json)))
}

/// 获取二级板块正负条形图数据
#[get("/sectionBar")]
pub async fn get_section_bar(data_service: web::Data<DataService>) -> Result<HttpResponse, AppError> {
    let section_bar_data = data_service
        .get_section_bar()
        .await
        .map_err(|e| AppError::DbError(e.to_string()))?;

    Ok(HttpResponse::Ok().json(ApiResponse::success(section_bar_data)))
}

/// 获取今日股票信息（用于定时任务调用）
#[get("/todayInfo")]
pub async fn get_today_info(data_service: web::Data<DataService>) -> Result<HttpResponse, AppError> {
    let result = data_service
        .get_today_info()
        .await
        .map_err(|e| AppError::DbError(e.to_string()))?;

    let json: Value =
        serde_json::from_str(&result).map_err(|e| AppError::JsonError(e.to_string()))?;

    Ok(HttpResponse::Ok().json(ApiResponse::success(json)))
}

pub fn init_routes(config: &mut web::ServiceConfig) {
    config
        .service(get_all_info)
        .service(get_sort_info)
        .service(get_section)
        .service(get_section_bar)
        .service(get_today_info);
}
