package com.galigeigei.acloudmap.infrastructure.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 申万字典实体类（数据库映射）
 * 基础设施层：与数据库表结构对应
 *
 * @author DDD实践
 * @since 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("a_sw_dict")
public class ASwDictEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;
}
