package com.galigeigei.acloudmap.infrastructure.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 股票基础信息实体类（数据库映射）
 * 基础设施层：与数据库表结构对应
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("a_info")
public class AInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 板块id
     */
    private String bkId;

    /**
     * 板块名称
     */
    private String bkName;

    /**
     * 二级板块
     */
    private String ejId;

    /**
     * 二级板块名称
     */
    private String ejName;

    /**
     * 所属交易所
     */
    private String exchange;
}
