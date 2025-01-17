use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use serde_json::Value;
use sqlx::FromRow;

/// 今日数据
#[derive(Debug, Serialize, Deserialize)]
pub struct AToday {
    /// 主键
    pub id: i32,
    /// 股票名称
    pub name: Option<String>,
    /// 股票代码
    pub code: Option<String>,
    /// 总市值
    pub total: Option<i64>,
    /// 当前价
    pub price: Option<f64>,
    /// 涨跌幅%
    pub increase: Option<f64>,
    /// 数据数组str
    pub arr_value: Option<String>,
    /// 数据数组
    pub value: Option<String>,
    /// 换手率
    pub turnover: Option<f64>,
    /// 上市日期
    pub into_date: Option<String>,
    /// 数据日期
    pub today: Option<String>,
    /// 创建时间
    pub creat_time: Option<DateTime<Utc>>,
}

/// 每日数据
#[derive(Debug, FromRow, Serialize, Deserialize)]
pub struct ADataJson {
    /// 主键
    pub id: i32,
    /// 个股数据
    pub json: Option<Value>,
    /// 板块数据
    pub section: Option<Value>,
    /// 数据日期
    pub today: Option<String>,
    /// 创建时间
    pub creat_time: Option<chrono::NaiveDateTime>,
}

/// 节假日表
#[derive(Debug, FromRow, Serialize, Deserialize)]
pub struct AHoliday {
    /// 主键
    pub id: i32,
    /// 是否为节假日: 0:非节假日, 1:节假日
    pub holiday: bool,
    /// 节日名称
    pub name: Option<String>,
    /// 日期
    pub date: Option<String>,
}

/// 股票基础信息
#[derive(Debug, Serialize, Deserialize)]
pub struct AInfo {
    /// 主键
    pub id: i32,
    /// 股票代码
    pub code: Option<String>,
    /// 名称
    pub name: Option<String>,
    /// 板块id
    pub bk_id: Option<String>,
    /// 板块名称
    pub bk_name: Option<String>,
    /// 二级板块
    pub ej_id: Option<String>,
    /// 二级板块名称
    pub ej_name: Option<String>,
    /// 所属交易所
    pub exchange: Option<String>,
}
