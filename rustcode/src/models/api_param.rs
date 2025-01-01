use serde::{Deserialize, Serialize};

/// 东方财富接口,个股基础信息
#[derive(Debug, Serialize, Deserialize)]
pub struct BaseInfo {
    /// 获取的参数字段
    pub f1: Option<i32>,
    /// 最新价
    pub f2: Option<f64>,
    /// 涨跌幅
    pub f3: Option<f64>,
    /// 涨跌额
    pub f4: Option<f64>,
    /// 成交量(手)
    pub f5: Option<i32>,
    /// 成交额(元)
    pub f6: Option<f64>,
    /// 振幅
    pub f7: Option<f64>,
    /// 换手率(%)
    pub f8: Option<f64>,
    /// 市盈率(动)
    pub f9: Option<f64>,
    /// 量比
    pub f10: Option<f64>,
    pub f11: Option<f64>,
    /// 股票代码
    pub f12: Option<String>,
    pub f13: Option<i32>,
    /// 股票名称
    pub f14: Option<String>,
    /// 最高价(元)
    pub f15: Option<f64>,
    /// 最低价(元)
    pub f16: Option<f64>,
    /// 开盘价(元)
    pub f17: Option<f64>,
    /// 昨日收盘价(元)
    pub f18: Option<f64>,
    pub f19: Option<i32>,
    /// 总市值(元)
    pub f20: Option<f64>,
    /// 流通市值(元)
    pub f21: Option<f64>,
    pub f22: Option<f64>,
    /// 市净率
    pub f23: Option<f64>,
    pub f24: Option<f64>,
    pub f25: Option<f64>,
    /// 上市日期:20240726
    pub f26: Option<String>,
    pub f27: Option<i32>,
    pub f28: Option<i32>,
    pub f29: Option<i32>,
}

