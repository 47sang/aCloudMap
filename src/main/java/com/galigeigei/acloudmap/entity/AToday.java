package com.galigeigei.acloudmap.entity;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 今日数据
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-27
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class AToday implements Serializable {

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
     * 涨跌幅
     */
    private Double increase;

    /**
     * 数据数组str
     */
    private String arrValue;
    /**
     * 数据数组
     */
    @TableField(exist = false)
    private JSONArray value;

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
