package com.galigeigei.acloudmap.infrastructure.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 节假日实体类（数据库映射）
 * 基础设施层：与数据库表结构对应
 *
 * @author DDD实践
 * @since 2024-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("a_holiday")
public class AHolidayEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否为节假日
     * 0:非节假日
     * 1:节假日
     */
    private Boolean holiday;

    /**
     * 节日名称
     */
    private String name;

    /**
     * 日期
     */
    private String date;
}
