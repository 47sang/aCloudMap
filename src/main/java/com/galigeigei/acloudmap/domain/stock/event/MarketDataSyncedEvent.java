package com.galigeigei.acloudmap.domain.stock.event;

import com.galigeigei.acloudmap.domain.shared.event.DomainEvent;
import com.galigeigei.acloudmap.domain.shared.valueobject.StockCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * 市场数据同步完成事件
 * 当从外部API获取并保存了最新的市场数据后触发
 * 用于通知其他限界上下文数据已更新
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public class MarketDataSyncedEvent extends DomainEvent {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据日期
     */
    private final LocalDate dataDate;
    
    /**
     * 同步的股票代码列表
     */
    private final List<StockCode> stockCodes;
    
    /**
     * 同步的股票数量
     */
    private final int stockCount;
    
    /**
     * 同步是否成功
     */
    private final boolean success;
    
    /**
     * 同步消息（成功或失败原因）
     */
    private final String message;
    
    public MarketDataSyncedEvent(LocalDate dataDate, List<StockCode> stockCodes, 
                                  int stockCount, boolean success, String message) {
        super();
        this.dataDate = dataDate;
        this.stockCodes = stockCodes;
        this.stockCount = stockCount;
        this.success = success;
        this.message = message;
    }
    
    /**
     * 创建成功事件
     */
    public static MarketDataSyncedEvent success(LocalDate dataDate, List<StockCode> stockCodes) {
        return new MarketDataSyncedEvent(dataDate, stockCodes, stockCodes.size(), true, "市场数据同步成功");
    }
    
    /**
     * 创建失败事件
     */
    public static MarketDataSyncedEvent failure(LocalDate dataDate, String message) {
        return new MarketDataSyncedEvent(dataDate, null, 0, false, message);
    }
}
