package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 市值值对象
 * 表示股票的市场价值，单位为元
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class MarketValue implements Serializable, Comparable<MarketValue> {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 市值值，单位：元
     */
    private final long value;
    
    /**
     * 私有构造函数
     */
    private MarketValue(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("市值不能为负数");
        }
        this.value = value;
    }
    
    /**
     * 工厂方法创建市值
     *
     * @param value 市值数值（元）
     * @return MarketValue 值对象
     */
    public static MarketValue of(long value) {
        return new MarketValue(value);
    }
    
    /**
     * 工厂方法：从亿元创建
     *
     * @param yiYuan 亿元
     * @return MarketValue 值对象
     */
    public static MarketValue fromYiYuan(double yiYuan) {
        return new MarketValue((long) (yiYuan * 100000000));
    }
    
    /**
     * 获取以亿元为单位的值
     */
    public double toYiYuan() {
        return BigDecimal.valueOf(value)
                .divide(BigDecimal.valueOf(100000000), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    /**
     * 市值相加
     */
    public MarketValue add(MarketValue other) {
        return new MarketValue(this.value + other.value);
    }
    
    /**
     * 计算涨跌额
     */
    public long subtract(MarketValue other) {
        return this.value - other.value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketValue that = (MarketValue) o;
        return value == that.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public int compareTo(MarketValue other) {
        return Long.compare(this.value, other.value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
