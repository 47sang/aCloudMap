package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 板块代码值对象
 * 表示申万行业分类代码
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class SectionCode implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String value;
    
    /**
     * 板块类型：一级或二级
     */
    private final SectionLevel level;
    
    /**
     * 私有构造函数
     */
    private SectionCode(String value, SectionLevel level) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("板块代码不能为空");
        }
        this.value = value.trim();
        this.level = level != null ? level : SectionLevel.UNKNOWN;
    }
    
    /**
     * 工厂方法创建板块代码
     *
     * @param value 板块代码字符串
     * @return SectionCode 值对象
     */
    public static SectionCode of(String value) {
        return new SectionCode(value, SectionLevel.UNKNOWN);
    }
    
    /**
     * 工厂方法创建带级别的板块代码
     *
     * @param value 板块代码字符串
     * @param level 板块级别
     * @return SectionCode 值对象
     */
    public static SectionCode of(String value, SectionLevel level) {
        return new SectionCode(value, level);
    }
    
    /**
     * 判断是否是一级板块
     */
    public boolean isPrimary() {
        return level == SectionLevel.PRIMARY;
    }
    
    /**
     * 判断是否是二级板块
     */
    public boolean isSecondary() {
        return level == SectionLevel.SECONDARY;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionCode that = (SectionCode) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    /**
     * 板块级别枚举
     */
    public enum SectionLevel {
        /**
         * 一级板块
         */
        PRIMARY,
        /**
         * 二级板块
         */
        SECONDARY,
        /**
         * 未知级别
         */
        UNKNOWN
    }
}
