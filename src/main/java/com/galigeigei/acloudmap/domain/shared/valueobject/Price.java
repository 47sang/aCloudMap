package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 价格值对象
 * 表示股票价格
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class Price implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 价格值，单位：元
     */
    private final double value;
    
    /**
     * 私有构造函数
     */
    private Price(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("价格不能为负数");
        }
        this.value = BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    /**
     * 工厂方法创建价格
     *
     * @param value 价格数值
     * @return Price 值对象
     */
    public static Price of(double value) {
        return new Price(value);
    }
    
    /**
     * 计算涨跌后的价格
     *
     * @param increaseRate 涨跌幅
     * @return 涨跌后的价格
     */
    public Price applyIncrease(IncreaseRate increaseRate) {
        double newValue = this.value * (1 + increaseRate.getValue() / 100);
        return new Price(newValue);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Double.compare(price.value, value) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return String.format("%.2f", value);
    }
}
