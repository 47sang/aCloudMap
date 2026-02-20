package com.galigeigei.acloudmap.domain.stock.service;

import com.galigeigei.acloudmap.domain.stock.event.MarketDataSyncedEvent;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import com.galigeigei.acloudmap.domain.stock.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 市场数据同步服务
 * 领域服务：处理市场数据同步的领域逻辑
 * 负责协调外部数据获取和领域对象更新
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Slf4j
@Service
public class MarketDataSyncService {
    
    private final StockRepository stockRepository;
    
    public MarketDataSyncService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    
    /**
     * 同步市场数据
     * 领域方法：将获取的市场数据保存到仓储
     *
     * @param stocks   股票数据列表
     * @param dataDate 数据日期
     * @return MarketDataSyncedEvent 同步完成事件
     */
    public MarketDataSyncedEvent syncMarketData(List<Stock> stocks, LocalDate dataDate) {
        try {
            if (stocks == null || stocks.isEmpty()) {
                log.warn("没有需要同步的市场数据");
                return MarketDataSyncedEvent.failure(dataDate, "没有需要同步的市场数据");
            }
            
            // 设置数据日期
            stocks.forEach(stock -> stock.setDataDate(dataDate));
            
            // 保存到仓储
            List<Stock> savedStocks = stockRepository.saveAll(stocks);
            
            log.info("成功同步 {} 只股票的市场数据", savedStocks.size());
            
            return MarketDataSyncedEvent.success(dataDate, 
                    savedStocks.stream().map(Stock::getCode).toList());
            
        } catch (Exception e) {
            log.error("同步市场数据失败", e);
            return MarketDataSyncedEvent.failure(dataDate, e.getMessage());
        }
    }
    
    /**
     * 检查数据是否已存在
     *
     * @param dataDate 数据日期
     * @return true 如果数据已存在
     */
    public boolean isDataExists(LocalDate dataDate) {
        return stockRepository.existsByDate(dataDate);
    }
    
    /**
     * 获取最新市场数据
     *
     * @return 最新的股票列表
     */
    public List<Stock> getLatestMarketData() {
        return stockRepository.findLatest();
    }
}
