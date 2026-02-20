package com.galigeigei.acloudmap.domain.section.event;

import com.galigeigei.acloudmap.domain.shared.event.DomainEvent;
import com.galigeigei.acloudmap.domain.shared.valueobject.SectionCode;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 板块数据聚合完成事件
 * 当板块数据完成聚合计算后触发
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public class SectionDataAggregatedEvent extends DomainEvent {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据日期
     */
    private final LocalDate dataDate;
    
    /**
     * 板块代码
     */
    private final SectionCode sectionCode;
    
    /**
     * 板块名称
     */
    private final String sectionName;
    
    /**
     * 聚合的股票数量
     */
    private final int stockCount;
    
    public SectionDataAggregatedEvent(LocalDate dataDate, SectionCode sectionCode, 
                                       String sectionName, int stockCount) {
        super();
        this.dataDate = dataDate;
        this.sectionCode = sectionCode;
        this.sectionName = sectionName;
        this.stockCount = stockCount;
    }
}
