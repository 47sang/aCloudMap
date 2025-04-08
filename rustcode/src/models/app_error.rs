use actix_web::{HttpResponse, ResponseError};
use crate::models::response::ApiResponse;

#[derive(Debug)]
pub enum AppError {
    DbError(String),
    JsonError(String),
    RequestError(String),
}

impl std::fmt::Display for AppError {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        match self {
            AppError::DbError(msg) => write!(f, "Database error: {}", msg),
            AppError::JsonError(msg) => write!(f, "JSON error: {}", msg),
            AppError::RequestError(msg) => write!(f, "Request error: {}", msg),
        }
    }
}

impl ResponseError for AppError {
    fn error_response(&self) -> HttpResponse {
        let error_message = match self {
            AppError::DbError(msg) => msg,
            AppError::JsonError(msg) => msg,
            AppError::RequestError(msg) => msg,
        };

        HttpResponse::Ok().json(ApiResponse::<()>::fail_with_code(-1, error_message))
    }
}

impl From<reqwest::Error> for AppError {
    fn from(err: reqwest::Error) -> Self {
        AppError::RequestError(err.to_string())
    }
}

impl From<serde_json::Error> for AppError {
    fn from(err: serde_json::Error) -> Self {
        AppError::JsonError(err.to_string())
    }
}