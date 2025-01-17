use serde::{Deserialize, Serialize};

/// 接口响应
#[derive(Debug, Serialize, Deserialize)]
pub struct ApiResponse<T> {
    pub code: i16,
    pub msg: String,
    pub data: Option<T>,
}

/// 接口响应实现
impl<T> ApiResponse<T> {
    /// 成功
    pub fn success(data: T) -> Self {
        Self {
            code: 200,
            msg: "成功".to_string(),
            data: Some(data),
        }
    }

    /// 失败
    pub fn fail(msg: &str) -> Self {
        Self {
            code: 500,
            msg: msg.to_string(),
            data: None,
        }
    }

    /// 失败
    pub fn fail_with_code(code: i16, msg: &str) -> Self {
        Self {
            code: code,
            msg: msg.to_string(),
            data: None,
        }
    }
}
