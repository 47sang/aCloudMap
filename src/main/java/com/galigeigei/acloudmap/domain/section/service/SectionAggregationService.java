package com.galigeigei.acloudmap.domain.section.service;

import com.galigeigei.acloudmap.domain.section.event.SectionDataAggregatedEvent;
import com.galigeigei.acloudmap.domain.section.model.Section;
import com.galigeigei.acloudmap.domain.shared.valueobject.SectionCode;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 板块聚合服务
 * 领域服务：处理涉及多个聚合的业务逻辑
 * 负责将股票数据聚合到板块结构中
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Service
public class SectionAggregationService {
    
    /**
     * 聚合板块数据
     * 将股票列表按照板块结构进行聚合
     *
     * @param sections 板块列表
     * @param stocks   股票列表
     * @return 聚合后的板块列表
     */
    public List<Section> aggregateSections(List<Section> sections, List<Stock> stocks) {
        // 按板块代码分组股票
        Map<SectionCode, List<Stock>> stocksBySection = stocks.stream()
                .filter(stock -> stock.getSecondarySectionCode() != null)
                .collect(Collectors.groupingBy(Stock::getSecondarySectionCode));
        
        // 将股票分配到对应板块
        for (Section section : sections) {
            List<Stock> sectionStocks = stocksBySection.getOrDefault(section.getCode(), List.of());
            sectionStocks.forEach(section::addStock);
        }
        
        return sections;
    }
    
    /**
     * 构建板块层级结构
     * 将一级板块和二级板块组织成树形结构
     *
     * @param primarySections   一级板块列表
     * @param secondarySections 二级板块列表
     * @return 构建好层级结构的一级板块列表
     */
    public List<Section> buildSectionHierarchy(List<Section> primarySections, 
                                                   List<Section> secondarySections) {
        // 按父板块代码分组二级板块
        Map<SectionCode, List<Section>> secondaryByParent = secondarySections.stream()
                .collect(Collectors.groupingBy(Section::getParentCode));
        
        // 将二级板块添加到对应的一级板块下
        for (Section primary : primarySections) {
            List<Section> children = secondaryByParent.getOrDefault(primary.getCode(), List.of());
            children.forEach(primary::addChildSection);
        }
        
        return primarySections;
    }
    
    /**
     * 按市值排序板块
     *
     * @param sections 板块列表
     * @return 按总市值降序排列的板块列表
     */
    public List<Section> sortSectionsByMarketValue(List<Section> sections) {
        return sections.stream()
                .sorted(Comparator.comparing(
                        Section::calculateTotalMarketValue, 
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }
    
    /**
     * 计算板块涨跌数据
     * 对比今日和昨日的板块数据，计算涨跌额和涨跌幅
     *
     * @param todaySections  今日板块数据
     * @param yesterdayData  昨日板块市值数据（板块代码 -> 市值）
     * @return 计算完成涨跌数据的板块列表
     */
    public List<Section> calculateSectionChanges(List<Section> todaySections,
                                                   Map<SectionCode, Long> yesterdayData) {
        for (Section section : todaySections) {
            Long yesterdayValue = yesterdayData.get(section.getCode());
            if (yesterdayValue != null) {
                // 计算涨跌额和涨跌幅
                // 这里可以扩展Section实体来存储这些计算值
            }
        }
        return todaySections;
    }
    
    /**
     * 创建板块聚合完成事件
     *
     * @param dataDate 数据日期
     * @param section  板块
     * @return SectionDataAggregatedEvent
     */
    public SectionDataAggregatedEvent createAggregatedEvent(LocalDate dataDate, Section section) {
        return new SectionDataAggregatedEvent(
                dataDate,
                section.getCode(),
                section.getName().toString(),
                section.getStockCount()
        );
    }
}
