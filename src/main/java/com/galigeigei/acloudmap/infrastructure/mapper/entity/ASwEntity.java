package com.galigeigei.acloudmap.infrastructure.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 申万板块实体类（数据库映射）
 * 基础设施层：与数据库表结构对应
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Data
@TableName("a_sw")
public class ASwEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 申万板块代码
     */
    private String swCode;

    /**
     * 板块名称
     */
    private String industryName;

    /**
     * 申万类型
     */
    private String industryType;

    /**
     * 来源
     */
    private String source;
}
