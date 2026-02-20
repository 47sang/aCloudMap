package com.galigeigei.acloudmap.domain.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件基类
 * 所有领域事件都继承此类
 * 领域事件用于在限界上下文之间传递信息
 *
 * @author DDD实践
 * @since 2024-07-26
 */
public abstract class DomainEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 事件唯一标识
     */
    private final String eventId;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    /**
     * 事件版本
     */
    private final int version;
    
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.version = 1;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    public int getVersion() {
        return version;
    }
}
