use crate::models::{AToday, ApiResponse, BaseInfo};
use crate::services::db_service::DbService;
use actix_web::{get, web, HttpResponse, Responder};
use reqwest::Client;
use serde::Serialize;
use std::collections::HashMap;
use std::env;
use serde_json::Value;

/// 辅助函数：将 Result 转换为 HTTP 响应
async fn handle_db_result<T: Serialize, E: std::error::Error + std::fmt::Display>(
    result: Result<T, E>,
) -> impl Responder {
    let response = match result {
        Ok(data) => ApiResponse::success(data),
        Err(e) => ApiResponse::fail(&e.to_string()),
    };
    HttpResponse::Ok().json(response)
}


/// 获取一二级分类整理后的所有股票信息
#[get("/all")]
pub async fn get_all_info(db: web::Data<DbService>) -> impl Responder {
  let section = db.get_json_only().await.expect("获取今日数据失败");
  let json: Value = serde_json::from_str::<Value>(&section.json.unwrap()).expect("json解析失败");
  HttpResponse::Ok().json(ApiResponse::success(json))
}

/// 按照市值排序的股票信息
#[get("/sort")]
pub async fn get_sort_info() -> impl Responder {
    let params = HashMap::from([
        ("pn", "1"),
        ("pz", "200"),
        ("po", "1"),
        ("np", "1"),
        ("ut", "bd1d9ddb04089700cf9c27f6f7426281"),
        ("fltt", "2"),
        ("invt", "2"),
        ("fid", "f20"),
        ("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048"),
        ("fields", "f2,f3,f8,f12,f14,f20,f26"),
        ("_", "1623833739532"),
    ]);

    let result = Client::new()
        .get(env::var("ALL_INFO").expect("未设置东方财富接口"))
        .query(&params)
        .send()
        .await;
    let result = result.expect("获取数据请求失败").text()
        .await
        .expect("东方财富接口获取数据失败")
        .replace("\"-\"", "0");
    let json = serde_json::from_str::<Value>(&result)
        .expect("json解析失败");
    let data = json.get("data").expect("data解析失败");
    let diff = data.get("diff").expect("diff解析失败");
    let diff_array = diff.as_array().expect("转换数组失败");

    let mut a_today_list: Vec<AToday> = vec![];

    for (i, item) in diff_array.iter().enumerate() {
        let base_info = serde_json::from_value::<BaseInfo>(item.clone()).expect("baseInfo解析失败");

        
        let mut a_today = AToday {
            id: i as i32 + 1,
            code: base_info.f12,
            name: base_info.f14,
            total: base_info.f20.map(|v| v as i64),
            price: Some(base_info.f2.expect("price解析失败") as f64),
            increase: Some(base_info.f3.expect("increase解析失败") as f64),
            turnover: base_info.f8,
            into_date: base_info.f26,
            //日期格式2025-1-17
            today: Some(chrono::Utc::now().format("%Y-%m-%d").to_string()),
            creat_time: chrono::Utc::now().into(),
            arr_value: None,
            value: None,
        };

        let arr: Value = serde_json::json!([
            a_today.total.unwrap_or(0),
            a_today.price.unwrap_or(0.0),
            a_today.increase.unwrap_or(0.0)
        ]);

        a_today.arr_value = Some(arr.to_string());
        a_today.value = Some(arr.to_string());
        a_today_list.push(a_today);
    }

    HttpResponse::Ok().json(ApiResponse::success(a_today_list))
    
}

/// 获取板块涨跌信息
#[get("/section")]
pub async fn get_section(db: web::Data<DbService>) -> impl Responder {
    let section = db.get_json_only().await.expect("获取板块涨跌信息失败");
    let json: Value = serde_json::from_str::<Value>(&section.section.unwrap()).expect("json解析失败");
    HttpResponse::Ok().json(ApiResponse::success(json))
}

/// 获取二级板块正负条形图数据
#[get("/sectionBar")]
pub async fn get_section_bar(db: web::Data<DbService>) -> impl Responder {
    HttpResponse::Ok().json(ApiResponse::success("未开发完成"))
}

pub fn init_routes(config: &mut web::ServiceConfig) {
    config
        .service(get_all_info)
        .service(get_sort_info)
        .service(get_section)
        .service(get_section_bar);
}
