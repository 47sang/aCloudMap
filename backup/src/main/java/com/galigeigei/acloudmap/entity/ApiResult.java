package com.galigeigei.acloudmap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 接口响应对象
 *
 * @author 周士钰
 * @date 2024/7/20 上午12:14
 */
@Data
@Accessors(chain = true)
public class ApiResult {

    int code;
    String msg;
    Object data;

    public static ApiResult success() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(200);
        apiResult.setMsg("成功");
        return apiResult;
    }

    public static ApiResult fail() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(500);
        apiResult.setMsg("失败");
        return apiResult;
    }

    public static ApiResult fail(String msg) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(500);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public ApiResult data(Object data) {
        this.setData(data);
        return this;
    }

    public ApiResult data(String key, Object value) {
        if (getData() == null) {
            Map<Object, Object> map = new HashMap<>();
            map.put(key, value);
            setData(map);
            return this;
        }
        if (getData() instanceof Map) {
            TreeMap<Object, Object> map = new TreeMap<>((Map<?, ?>) getData());
            map.put(key, value);
            setData(map);
            return this;
        }
        throw new UnsupportedOperationException("data不是Map类型,不能插入数据");
    }
}
