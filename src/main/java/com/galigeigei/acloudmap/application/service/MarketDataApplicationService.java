package com.galigeigei.acloudmap.application.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.galigeigei.acloudmap.domain.section.model.Section;
import com.galigeigei.acloudmap.domain.section.repository.SectionRepository;
import com.galigeigei.acloudmap.domain.section.service.SectionAggregationService;
import com.galigeigei.acloudmap.domain.shared.valueobject.SectionCode;
import com.galigeigei.acloudmap.domain.stock.event.MarketDataSyncedEvent;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import com.galigeigei.acloudmap.domain.stock.service.MarketDataSyncService;
import com.galigeigei.acloudmap.infrastructure.external.EastMoneyApiClient;
import com.galigeigei.acloudmap.infrastructure.mapper.ADataJsonMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ADataJsonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 市场数据应用服务
 * 应用层：协调领域层完成用例，处理事务边界
 * 负责市场数据的同步和持久化
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Slf4j
@Service
public class MarketDataApplicationService {
    
    private final MarketDataSyncService marketDataSyncService;
    private final SectionAggregationService sectionAggregationService;
    private final EastMoneyApiClient eastMoneyApiClient;
    private final SectionRepository sectionRepository;
    private final ADataJsonMapper aDataJsonMapper;
    
    public MarketDataApplicationService(MarketDataSyncService marketDataSyncService,
                                        SectionAggregationService sectionAggregationService,
                                        EastMoneyApiClient eastMoneyApiClient,
                                        SectionRepository sectionRepository,
                                        ADataJsonMapper aDataJsonMapper) {
        this.marketDataSyncService = marketDataSyncService;
        this.sectionAggregationService = sectionAggregationService;
        this.eastMoneyApiClient = eastMoneyApiClient;
        this.sectionRepository = sectionRepository;
        this.aDataJsonMapper = aDataJsonMapper;
    }
    
    /**
     * 同步今日市场数据
     * 用例：定时任务调用
     *
     * @return 同步结果事件
     */
    @Transactional
    public MarketDataSyncedEvent syncTodayMarketData() {
        LocalDate today = LocalDate.now();
        
        // 获取市场数据
        List<Stock> stocks = eastMoneyApiClient.fetchTodayMarketData();
        
        if (stocks.isEmpty()) {
            log.warn("获取市场数据为空");
            return MarketDataSyncedEvent.failure(today, "获取市场数据为空");
        }
        
        // 同步数据到领域层
        MarketDataSyncedEvent event = marketDataSyncService.syncMarketData(stocks, today);
        
        if (event.isSuccess()) {
            // 聚合板块数据并保存JSON
            saveAggregatedData(stocks, today);
        }
        
        return event;
    }
    
    /**
     * 保存聚合后的数据
     */
    private void saveAggregatedData(List<Stock> stocks, LocalDate dataDate) {
        try {
            // 获取板块数据
            List<Section> primarySections = sectionRepository.findAllPrimarySections();
            List<Section> allSections = new ArrayList<>();
            
            for (Section primary : primarySections) {
                allSections.add(primary);
                List<Section> secondarySections = sectionRepository.findSecondarySectionsByParentCode(primary.getCode());
                allSections.addAll(secondarySections);
            }
            
            // 聚合板块数据
            sectionAggregationService.aggregateSections(allSections, stocks);
            
            // 构建层级结构
            List<Section> hierarchy = sectionAggregationService.buildSectionHierarchy(
                    primarySections,
                    allSections.stream()
                            .filter(s -> s.getCode().getLevel() == SectionCode.SectionLevel.SECONDARY)
                            .collect(Collectors.toList())
            );
            
            // 按市值排序
            List<Section> sortedSections = sectionAggregationService.sortSectionsByMarketValue(hierarchy);
            
            // 转换为JSON并保存
            JSONArray jsonArray = convertToJsonArray(sortedSections);
            
            ADataJsonEntity entity = new ADataJsonEntity();
            entity.setJson(jsonArray.toJSONString());
            entity.setToday(dataDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            entity.setCreatTime(new Date());
            
            aDataJsonMapper.insert(entity);
            
            log.info("成功保存聚合数据，日期：{}", dataDate);
            
        } catch (Exception e) {
            log.error("保存聚合数据失败", e);
        }
    }
    
    /**
     * 转换为JSON数组
     */
    private JSONArray convertToJsonArray(List<Section> sections) {
        JSONArray array = new JSONArray();
        for (Section section : sections) {
            array.add(convertToJsonObject(section));
        }
        return array;
    }
    
    /**
     * 将板块转换为JSONObject
     */
    private JSONObject convertToJsonObject(Section section) {
        JSONObject json = new JSONObject();
        json.put("name", section.getName().getValue());
        json.put("code", section.getCode().getValue());
        json.put("total", section.calculateTotalMarketValue().getValue());
        json.put("value", section.getValueArray());
        
        JSONArray childrenArray = new JSONArray();
        for (Section child : section.getChildren()) {
            childrenArray.add(convertChildToJsonObject(child));
        }
        json.put("children", childrenArray);
        
        return json;
    }
    
    /**
     * 将子板块转换为JSONObject
     */
    private JSONObject convertChildToJsonObject(Section section) {
        JSONObject json = new JSONObject();
        json.put("name", section.getName().getValue());
        json.put("code", section.getCode().getValue());
        json.put("total", section.calculateTotalMarketValue().getValue());
        json.put("value", section.getValueArray());
        
        JSONArray stocksArray = new JSONArray();
        for (Stock stock : section.getStocksSortedByMarketValue()) {
            JSONObject stockJson = new JSONObject();
            stockJson.put("code", stock.getCode().getValue());
            stockJson.put("name", stock.getName().getValue());
            stockJson.put("total", stock.getMarketValue().getValue());
            stockJson.put("price", stock.getCurrentPrice().getValue());
            stockJson.put("increase", stock.getIncreaseRate().getValue());
            stockJson.put("value", stock.getValueArray());
            stockJson.put("today", stock.getDataDate() != null ? stock.getDataDate().toString() : "");
            stocksArray.add(stockJson);
        }
        json.put("children", stocksArray);
        
        return json;
    }
}
