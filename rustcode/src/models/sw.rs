use serde::{Deserialize, Serialize};

/// 申万板块
#[derive(Debug, Serialize, Deserialize)]
pub struct ASw {
    /// 主键
    pub id: i32,
    /// 股票代码
    pub stock_code: Option<String>,
    /// 申万板块代码
    pub sw_code: Option<String>,
    /// 板块名称
    pub industry_name: Option<String>,
    /// 申万类型
    pub industry_type: Option<String>,
    /// 来源
    pub source: Option<String>,
}

/// 申万字典
#[derive(Debug, Serialize, Deserialize)]
pub struct ASwDict {
    /// 代码
    pub code: Option<String>,
    /// 名称
    pub name: Option<String>,
    /// 类型
    pub type_: Option<String>,
}