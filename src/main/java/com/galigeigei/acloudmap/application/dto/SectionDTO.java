package com.galigeigei.acloudmap.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 板块数据传输对象
 * 应用层：用于接口层和领域层之间的数据传输
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Data
public class SectionDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 板块代码
     */
    private String code;
    
    /**
     * 板块名称
     */
    private String name;
    
    /**
     * 板块总市值
     */
    private Long total;
    
    /**
     * 数据值数组
     */
    private List<Long> value;
    
    /**
     * 子板块或股票列表
     */
    private List<Object> children;
    
    /**
     * 涨跌幅
     */
    private Double increase;
    
    /**
     * 涨跌额
     */
    private Double turnover;
}
