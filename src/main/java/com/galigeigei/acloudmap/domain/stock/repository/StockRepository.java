package com.galigeigei.acloudmap.domain.stock.repository;

import com.galigeigei.acloudmap.domain.shared.valueobject.StockCode;
import com.galigeigei.acloudmap.domain.stock.model.Stock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 股票仓储接口
 * 属于领域层，定义领域对象的持久化操作
 * 具体实现在基础设施层
 *
 * @author DDD实践
 * @since 2024-07-26
 */
public interface StockRepository {
    
    /**
     * 根据股票代码查找股票
     *
     * @param code 股票代码
     * @return Optional<Stock>
     */
    Optional<Stock> findByCode(StockCode code);
    
    /**
     * 根据股票代码和数据日期查找股票
     *
     * @param code     股票代码
     * @param dataDate 数据日期
     * @return Optional<Stock>
     */
    Optional<Stock> findByCodeAndDate(StockCode code, LocalDate dataDate);
    
    /**
     * 查找指定日期的所有股票
     *
     * @param dataDate 数据日期
     * @return 股票列表
     */
    List<Stock> findByDate(LocalDate dataDate);
    
    /**
     * 查找指定日期的最新数据（不指定日期则取最新）
     *
     * @return 股票列表
     */
    List<Stock> findLatest();
    
    /**
     * 保存股票
     *
     * @param stock 股票实体
     * @return 保存后的股票
     */
    Stock save(Stock stock);
    
    /**
     * 批量保存股票
     *
     * @param stocks 股票列表
     * @return 保存后的股票列表
     */
    List<Stock> saveAll(List<Stock> stocks);
    
    /**
     * 检查指定日期的数据是否存在
     *
     * @param dataDate 数据日期
     * @return true 如果存在
     */
    boolean existsByDate(LocalDate dataDate);
}
