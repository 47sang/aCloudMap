package com.galigeigei.acloudmap.application.assembler;

import com.galigeigei.acloudmap.application.dto.StockDTO;
import com.galigeigei.acloudmap.domain.stock.model.Stock;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 股票组装器
 * 应用层：负责领域对象和DTO之间的转换
 * 遵循DDD的防腐层模式
 *
 * @author DDD实践
 * @since 2024-07-26
 */
public class StockAssembler {
    
    /**
     * 将领域对象转换为DTO
     *
     * @param stock 股票领域对象
     * @return StockDTO
     */
    public static StockDTO toDTO(Stock stock) {
        if (stock == null) {
            return null;
        }
        StockDTO dto = new StockDTO();
        dto.setCode(stock.getCode().getValue());
        dto.setName(stock.getName().getValue());
        if (stock.getCurrentPrice() != null) {
            dto.setPrice(stock.getCurrentPrice().getValue());
        }
        if (stock.getMarketValue() != null) {
            dto.setTotal(stock.getMarketValue().getValue());
        }
        if (stock.getIncreaseRate() != null) {
            dto.setIncrease(stock.getIncreaseRate().getValue());
        }
        if (stock.getTurnoverRate() != null) {
            dto.setTurnover(stock.getTurnoverRate().getValue());
        }
        dto.setIntoDate(stock.getListingDate());
        if (stock.getDataDate() != null) {
            dto.setToday(stock.getDataDate().toString());
        }
        dto.setValue(stock.getValueArray());
        return dto;
    }
    
    /**
     * 将领域对象列表转换为DTO列表
     *
     * @param stocks 股票领域对象列表
     * @return StockDTO列表
     */
    public static List<StockDTO> toDTOList(List<Stock> stocks) {
        return stocks.stream()
                .map(StockAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
