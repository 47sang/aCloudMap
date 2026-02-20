package com.galigeigei.acloudmap.infrastructure.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 今日数据实体类（数据库映射）
 * 基础设施层：与数据库表结构对应
 *
 * @author DDD实践
 * @since 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("a_today")
public class ATodayEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 总市值
     */
    private Long total;

    /**
     * 当前价
     */
    private Double price;

    /**
     * 涨跌幅%
     */
    private Double increase;

    /**
     * 数据数组str
     */
    private String arrValue;

    /**
     * 换手率
     */
    private Double turnover;

    /**
     * 上市日期
     */
    private String intoDate;

    /**
     * 数据日期
     */
    private String today;

    /**
     * 创建时间
     */
    private Date creatTime;
}
