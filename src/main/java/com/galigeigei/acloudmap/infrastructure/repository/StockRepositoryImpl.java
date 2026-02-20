package com.galigeigei.acloudmap.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.galigeigei.acloudmap.domain.shared.valueobject.*;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import com.galigeigei.acloudmap.domain.stock.repository.StockRepository;
import com.galigeigei.acloudmap.infrastructure.mapper.ATodayMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ATodayEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 股票仓储实现
 * 基础设施层：实现领域层定义的仓储接口
 * 负责领域对象与数据库实体之间的转换
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Repository
public class StockRepositoryImpl implements StockRepository {
    
    private final ATodayMapper aTodayMapper;
    
    public StockRepositoryImpl(ATodayMapper aTodayMapper) {
        this.aTodayMapper = aTodayMapper;
    }
    
    @Override
    public Optional<Stock> findByCode(StockCode code) {
        LambdaQueryWrapper<ATodayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ATodayEntity::getCode, code.getValue())
               .orderByDesc(ATodayEntity::getId)
               .last("LIMIT 1");
        ATodayEntity entity = aTodayMapper.selectOne(wrapper);
        return Optional.ofNullable(convertToDomain(entity));
    }
    
    @Override
    public Optional<Stock> findByCodeAndDate(StockCode code, LocalDate dataDate) {
        String dateStr = dataDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LambdaQueryWrapper<ATodayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ATodayEntity::getCode, code.getValue())
               .eq(ATodayEntity::getToday, dateStr);
        ATodayEntity entity = aTodayMapper.selectOne(wrapper);
        return Optional.ofNullable(convertToDomain(entity));
    }
    
    @Override
    public List<Stock> findByDate(LocalDate dataDate) {
        String dateStr = dataDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LambdaQueryWrapper<ATodayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ATodayEntity::getToday, dateStr);
        List<ATodayEntity> entities = aTodayMapper.selectList(wrapper);
        return entities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Stock> findLatest() {
        List<ATodayEntity> entities = aTodayMapper.selectLatest();
        return entities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Stock save(Stock stock) {
        ATodayEntity entity = convertToEntity(stock);
        if (entity.getId() == null) {
            aTodayMapper.insert(entity);
        } else {
            aTodayMapper.updateById(entity);
        }
        stock.setId(entity.getId());
        return stock;
    }
    
    @Override
    public List<Stock> saveAll(List<Stock> stocks) {
        return stocks.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByDate(LocalDate dataDate) {
        String dateStr = dataDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LambdaQueryWrapper<ATodayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ATodayEntity::getToday, dateStr);
        return aTodayMapper.selectCount(wrapper) > 0;
    }
    
    /**
     * 将数据库实体转换为领域对象
     */
    private Stock convertToDomain(ATodayEntity entity) {
        if (entity == null) {
            return null;
        }
        Stock stock = Stock.create(
                entity.getCode(),
                entity.getName(),
                entity.getPrice() != null ? entity.getPrice() : 0,
                entity.getTotal() != null ? entity.getTotal() : 0,
                entity.getIncrease() != null ? entity.getIncrease() : 0,
                entity.getTurnover() != null ? entity.getTurnover() : 0
        );
        stock.setId(entity.getId());
        stock.setListingDate(entity.getIntoDate());
        if (entity.getToday() != null) {
            stock.setDataDate(LocalDate.parse(entity.getToday()));
        }
        return stock;
    }
    
    /**
     * 将领域对象转换为数据库实体
     */
    private ATodayEntity convertToEntity(Stock stock) {
        if (stock == null) {
            return null;
        }
        ATodayEntity entity = new ATodayEntity();
        entity.setId(stock.getId());
        entity.setCode(stock.getCode().getValue());
        entity.setName(stock.getName().getValue());
        if (stock.getCurrentPrice() != null) {
            entity.setPrice(stock.getCurrentPrice().getValue());
        }
        if (stock.getMarketValue() != null) {
            entity.setTotal(stock.getMarketValue().getValue());
        }
        if (stock.getIncreaseRate() != null) {
            entity.setIncrease(stock.getIncreaseRate().getValue());
        }
        if (stock.getTurnoverRate() != null) {
            entity.setTurnover(stock.getTurnoverRate().getValue());
        }
        entity.setIntoDate(stock.getListingDate());
        if (stock.getDataDate() != null) {
            entity.setToday(stock.getDataDate().toString());
        }
        return entity;
    }
}
