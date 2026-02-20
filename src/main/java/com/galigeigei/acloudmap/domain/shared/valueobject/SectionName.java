package com.galigeigei.acloudmap.domain.shared.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 板块名称值对象
 * 值对象是不可变的
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public final class SectionName implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String value;
    
    /**
     * 私有构造函数
     */
    private SectionName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("板块名称不能为空");
        }
        this.value = value.trim();
    }
    
    /**
     * 工厂方法创建板块名称
     *
     * @param name 板块名称字符串
     * @return SectionName 值对象
     */
    public static SectionName of(String name) {
        return new SectionName(name);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionName that = (SectionName) o;
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
}
