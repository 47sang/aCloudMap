package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 涨跌幅值对象
 * 表示股票价格变动的百分比
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class IncreaseRate implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 涨跌幅值，单位：%
     * 正数表示上涨，负数表示下跌
     */
    private final double value;
    
    /**
     * 私有构造函数
     */
    private IncreaseRate(double value) {
        // 涨跌幅可以超过10%，不限制范围
        this.value = BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    /**
     * 工厂方法创建涨跌幅
     *
     * @param value 涨跌幅数值（%）
     * @return IncreaseRate 值对象
     */
    public static IncreaseRate of(double value) {
        return new IncreaseRate(value);
    }
    
    /**
     * 根据涨跌额和基准市值计算涨跌幅
     *
     * @param changeAmount 涨跌额
     * @param baseValue    基准市值
     * @return IncreaseRate 值对象
     */
    public static IncreaseRate calculate(double changeAmount, double baseValue) {
        if (baseValue == 0) {
            return new IncreaseRate(0);
        }
        double rate = (changeAmount / baseValue) * 100;
        return new IncreaseRate(rate);
    }
    
    /**
     * 判断是否为上涨
     */
    public boolean isRising() {
        return value > 0;
    }
    
    /**
     * 判断是否为下跌
     */
    public boolean isFalling() {
        return value < 0;
    }
    
    /**
     * 判断是否持平
     */
    public boolean isFlat() {
        return value == 0;
    }
    
    /**
     * 获取格式化后的涨跌幅字符串
     */
    public String getFormattedValue() {
        String sign = value > 0 ? "+" : "";
        return sign + String.format("%.2f%%", value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncreaseRate that = (IncreaseRate) o;
        return Double.compare(that.value, value) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return getFormattedValue();
    }
}
