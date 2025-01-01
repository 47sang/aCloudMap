use serde::{Deserialize, Serialize};

/// 大盘云图个股涨跌数据
#[derive(Debug, Serialize, Deserialize)]
pub struct DataBO {
    /// 股票代码
    pub code: String,
    /// 股票名称
    pub name: String,
    /// 总市值
    pub total: i32,
    /// 数据数组
    pub value: Vec<i32>,
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
    pub total: i32,
    /// 数据数组
    pub value: Vec<i32>,
    /// 子数据
    pub children: Vec<ChildrenDTO3>,
}

/// 子数据3
#[derive(Debug, Serialize, Deserialize)]
pub struct ChildrenDTO3 {
    /// id
    pub id: i32,
    /// 股票代码
    pub code: String,
    /// 股票名称
    pub name: String,
    /// 价格
    pub price: f64,
    /// 今日
    pub today: String,
    /// 总市值
    pub total: i32,
    /// 值
    pub value: Vec<i32>,
    /// 数组值
    pub arr_value: String,
    /// 涨跌幅%
    pub increase: f64,
    /// 上市日期
    pub into_date: String,
    /// 换手率
    pub turnover: f64,
    /// 创建时间
    pub creat_time: String,
}

/// 二级板块涨跌数据
#[derive(Debug, Serialize, Deserialize)]
pub struct SectionBO {
    /// 股票代码
    pub code: String,
    /// 股票名称
    pub name: String,
    /// 总市值
    pub total: i32,
    /// 数据数组
    pub value: Vec<i32>,
    /// 子数据
    pub children: Vec<ChildrenDTO3>,
}