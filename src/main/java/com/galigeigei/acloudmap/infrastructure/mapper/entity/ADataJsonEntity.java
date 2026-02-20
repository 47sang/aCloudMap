package com.galigeigei.acloudmap.infrastructure.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日数据实体类（数据库映射）
 * 基础设施层：与数据库表结构对应
 *
 * @author DDD实践
 * @since 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("a_data_json")
public class ADataJsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 个股数据
     */
    private String json;

    /**
     * 板块数据
     */
    private String section;

    /**
     * 数据日期
     */
    private String today;

    /**
     * 创建时间
     */
    private Date creatTime;
}
