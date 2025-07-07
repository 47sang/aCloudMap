use serde::{Deserialize, Serialize};

/// 大盘云图个股涨跌数据
#[derive(Debug, Serialize, Deserialize)]
pub struct DataBO {
    /// 股票代码
    pub code: String,
    /// 股票名称
    pub name: String,
    /// 总市值
    pub total: f64,
    /// 数据数组
    pub value: Vec<f64>,
    /// 子数据
    pub children: Vec<ChildrenDTO>,
}

/// 子数据
#[derive(Debug, Serialize, Deserialize)]
pub struct ChildrenDTO {
    /// 股票代码
    pub code: String,
    /// 股票名称
    pub name: String,
    /// 总市值
    pub total: f64,
    /// 涨跌幅%
    pub increase: f64,
    /// 换手率/涨跌额
    pub turnover: f64,
    /// 数据数组
    pub value: Vec<f64>,
    /// 子数据
    pub children: Vec<ChildrenDTO3>,
}

/// 子数据3
#[derive(Debug, Serialize, Deserialize)]
pub struct ChildrenDTO3 {
    /// id
    pub id: i32,
    /// 股票代码
    pub code: Option<String>,
    /// 股票名称
    pub name: Option<String>,
    /// 价格
    pub price: Option<f64>,
    /// 今日
    pub today: Option<String>,
    /// 总市值
    pub total: Option<i64>,
    /// 值
    pub value: Option<String>,
    /// 数组值
    pub arr_value: Option<String>,
    /// 涨跌幅%
    pub increase: Option<f64>,
    /// 上市日期
    pub into_date: Option<String>,
    /// 换手率
    pub turnover: Option<f64>,
    /// 创建时间
    pub creat_time: Option<String>,
}

/// 二级板块涨跌数据
#[derive(Debug, Serialize, Deserialize)]
pub struct SectionBO {
    /// 股票代码
    pub code: String,
    /// 股票名称
    pub name: String,
    /// 总市值
    pub total: f64,
    /// 数据数组
    pub value: Vec<f64>,
    /// 子数据
    pub children: Vec<ChildrenDTO>,
}