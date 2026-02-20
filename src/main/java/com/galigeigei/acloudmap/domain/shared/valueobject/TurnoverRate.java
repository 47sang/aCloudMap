package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 换手率值对象
 * 表示股票交易的活跃程度
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class TurnoverRate implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 换手率值，单位：%
     */
    private final double value;
    
    /**
     * 私有构造函数
     */
    private TurnoverRate(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("换手率不能为负数");
        }
        this.value = BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    
    /**
     * 工厂方法创建换手率
     *
     * @param value 换手率数值（%）
     * @return TurnoverRate 值对象
     */
    public static TurnoverRate of(double value) {
        return new TurnoverRate(value);
    }
    
    /**
     * 判断是否为活跃交易
     * 换手率 > 5% 视为活跃
     */
    public boolean isActive() {
        return value > 5.0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnoverRate that = (TurnoverRate) o;
        return Double.compare(that.value, value) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return String.format("%.2f%%", value);
    }
}
