use serde::{Deserialize, Serialize};

/// 二级板块正负条形图数据
#[derive(Debug, Serialize, Deserialize)]
pub struct SectionBarVo {
    /// 分类名称
    pub category_name: Vec<String>,
    /// 当前市值
    pub now_num: Vec<f64>,
    /// 昨日市值
    pub yesterday_num: Vec<f64>,
    /// 涨跌额
    pub change_amount: Vec<f64>,
}