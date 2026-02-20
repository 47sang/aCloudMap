package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 股票代码值对象
 * 值对象是不可变的，通过其属性值来定义相等性
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class StockCode implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String value;
    
    /**
     * 私有构造函数，通过工厂方法创建
     */
    private StockCode(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("股票代码不能为空");
        }
        this.value = value.trim();
    }
    
    /**
     * 工厂方法创建股票代码
     *
     * @param code 股票代码字符串
     * @return StockCode 值对象
     */
    public static StockCode of(String code) {
        return new StockCode(code);
    }
    
    /**
     * 获取带交易所前缀的完整代码
     * 上海: sh, 深圳: sz
     */
    public String getFullCode() {
        // 根据代码规则判断交易所
        if (value.startsWith("6") || value.startsWith("5")) {
            return "sh" + value;
        } else {
            return "sz" + value;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockCode stockCode = (StockCode) o;
        return Objects.equals(value, stockCode.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
