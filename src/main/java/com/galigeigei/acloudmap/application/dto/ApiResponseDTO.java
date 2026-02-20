package com.galigeigei.acloudmap.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * API响应数据传输对象
 * 应用层：统一的接口响应格式
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Data
public class ApiResponseDTO<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String msg;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 创建成功响应
     */
    public static <T> ApiResponseDTO<T> success(T data) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setCode(200);
        response.setMsg("成功");
        response.setData(data);
        return response;
    }
    
    /**
     * 创建失败响应
     */
    public static <T> ApiResponseDTO<T> fail(String message) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setCode(500);
        response.setMsg(message);
        return response;
    }
    
    /**
     * 创建自定义响应
     */
    public static <T> ApiResponseDTO<T> of(int code, String msg, T data) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }
}
