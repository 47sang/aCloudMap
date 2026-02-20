package com.galigeigei.acloudmap.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 股票数据传输对象
 * 应用层：用于接口层和领域层之间的数据传输
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Data
public class StockDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 股票代码
     */
    private String code;
    
    /**
     * 股票名称
     */
    private String name;
    
    /**
     * 当前价格
     */
    private Double price;
    
    /**
     * 总市值
     */
    private Long total;
    
    /**
     * 涨跌幅
     */
    private Double increase;
    
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
     * 数据值数组（用于前端展示）
     */
    private long[] value;
}
