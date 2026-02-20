package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 股票名称值对象
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class StockName implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String value;
    
    /**
     * 私有构造函数
     */
    private StockName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("股票名称不能为空");
        }
        this.value = value.trim();
    }
    
    /**
     * 工厂方法创建股票名称
     *
     * @param name 股票名称字符串
     * @return StockName 值对象
     */
    public static StockName of(String name) {
        return new StockName(name);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockName stockName = (StockName) o;
        return Objects.equals(value, stockName.value);
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
