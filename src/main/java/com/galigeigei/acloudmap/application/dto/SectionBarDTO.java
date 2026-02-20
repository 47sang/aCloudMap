package com.galigeigei.acloudmap.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 板块条形图数据传输对象
 * 应用层：用于前端条形图展示
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Data
public class SectionBarDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 分类名称
     */
    private List<String> categoryName;
    
    /**
     * 当前市值
     */
    private List<Double> nowNum;
    
    /**
     * 昨日市值
     */
    private List<Double> yesterdayNum;
    
    /**
     * 涨跌额
     */
    private List<Double> changeAmount;
}
