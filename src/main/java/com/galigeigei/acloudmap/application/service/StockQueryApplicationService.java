package com.galigeigei.acloudmap.application.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.galigeigeigei.acloudmap.application.assembler.SectionAssembler;
import com.galigeigei.acloudmap.application.assembler.StockAssembler;
import com.galigeigei.acloudmap.application.dto.ApiResponseDTO;
import com.galigeigei.acloudmap.application.dto.SectionBarDTO;
import com.galigeigei.acloudmap.application.dto.StockDTO;
import com.galigeigei.acloudmap.domain.section.model.Section;
import com.galigeigei.acloudmap.domain.section.repository.SectionRepository;
import com.galigeigei.acloudmap.domain.section.service.SectionAggregationService;
import com.galigeigei.acloudmap.domain.shared.valueobject.SectionCode;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import com.galigeigei.acloudmap.domain.stock.repository.StockRepository;
import com.galigeigei.acloudmap.infrastructure.external.EastMoneyApiClient;
import com.galigeigei.acloudmap.infrastructure.mapper.ADataJsonMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ADataJsonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 股票查询应用服务
 * 应用层：协调领域层完成用例，处理事务边界
 * 负责股票数据的查询和展示逻辑
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Slf4j
@Service
public class StockQueryApplicationService {
    
    private final StockRepository stockRepository;
    private final SectionRepository sectionRepository;
    private final SectionAggregationService sectionAggregationService;
    private final EastMoneyApiClient eastMoneyApiClient;
    private final ADataJsonMapper aDataJsonMapper;
    
    public StockQueryApplicationService(StockRepository stockRepository,
                                        SectionRepository sectionRepository,
                                        SectionAggregationService sectionAggregationService,
                                        EastMoneyApiClient eastMoneyApiClient,
                                        ADataJsonMapper aDataJsonMapper) {
        this.stockRepository = stockRepository;
        this.sectionRepository = sectionRepository;
        this.sectionAggregationService = sectionAggregationService;
        this.eastMoneyApiClient = eastMoneyApiClient;
        this.aDataJsonMapper = aDataJsonMapper;
    }
    
    /**
     * 获取所有股票信息（按板块聚合）
     * 用例：大盘云图展示
     *
     * @return API响应
     */
    public ApiResponseDTO<JSONArray> getAllStockInfo() {
        // 先尝试从缓存获取
        ADataJsonEntity todayData = aDataJsonMapper.selectLatest();
        if (todayData != null && todayData.getJson() != null) {
            JSONArray jsonArray = JSONArray.parseArray(todayData.getJson());
            return ApiResponseDTO.success(jsonArray);
        }
        
        // 从API获取最新数据
        return getTodayStockInfo();
    }
    
    /**
     * 获取今日股票信息
     *
     * @return API响应
     */
    public ApiResponseDTO<JSONArray> getTodayStockInfo() {
        // 获取股票数据
        List<Stock> stocks = eastMoneyApiClient.fetchTodayMarketData();
        
        // 获取板块数据
        List<Section> primarySections = sectionRepository.findAllPrimarySections();
        List<Section> allSections = new ArrayList<>();
        
        // 获取所有二级板块
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
        
        // 转换为JSON
        JSONArray result = SectionAssembler.toJSONArray(sortedSections);
        
        return ApiResponseDTO.success(result);
    }
    
    /**
     * 获取按市值排序的股票信息
     * 用例：市值排序展示
     *
     * @return API响应
     */
    public ApiResponseDTO<List<StockDTO>> getStocksSortedByMarketValue() {
        List<Stock> stocks = eastMoneyApiClient.fetchMarketDataSortedByMarketValue();
        
        // 设置序号
        List<StockDTO> dtos = new ArrayList<>();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            StockDTO dto = StockAssembler.toDTO(stock);
            dtos.add(dto);
        }
        
        return ApiResponseDTO.success(dtos);
    }
    
    /**
     * 获取板块信息
     * 用例：板块云图展示
     *
     * @return API响应
     */
    public ApiResponseDTO<JSONArray> getSectionInfo() {
        // 先尝试从缓存获取
        ADataJsonEntity todayData = aDataJsonMapper.selectLatest();
        if (todayData != null && todayData.getSection() != null) {
            JSONArray jsonArray = JSONArray.parseArray(todayData.getSection());
            return ApiResponseDTO.success(jsonArray);
        }
        
        // 如果没有缓存，获取今日数据
        return getTodayStockInfo();
    }
    
    /**
     * 获取板块条形图数据
     * 用例：板块涨跌条形图展示
     *
     * @return API响应
     */
    public ApiResponseDTO<SectionBarDTO> getSectionBarData() {
        ADataJsonEntity todayData = aDataJsonMapper.selectLatest();
        if (todayData == null || todayData.getSection() == null) {
            return ApiResponseDTO.fail("暂无数据");
        }
        
        // 解析板块数据
        JSONArray sectionArray = JSONArray.parseArray(todayData.getSection());
        
        // 提取二级板块数据
        List<SectionBarItem> items = new ArrayList<>();
        for (int i = 0; i < sectionArray.size(); i++) {
            JSONObject section = sectionArray.getJSONObject(i);
            JSONArray children = section.getJSONArray("children");
            if (children != null) {
                for (int j = 0; j < children.size(); j++) {
                    JSONObject child = children.getJSONObject(j);
                    SectionBarItem item = new SectionBarItem();
                    item.setName(child.getString("name"));
                    item.setTotal(child.getDoubleValue("total"));
                    item.setIncrease(child.getDoubleValue("increase"));
                    item.setTurnover(child.getDoubleValue("turnover"));
                    items.add(item);
                }
            }
        }
        
        // 按涨跌额排序
        items.sort(Comparator.comparingDouble(SectionBarItem::getTurnover).reversed());
        
        // 添加序号
        for (int i = 0; i < items.size(); i++) {
            SectionBarItem item = items.get(i);
            item.setName((i + 1) + "." + item.getName());
        }
        
        // 按总市值排序
        items.sort(Comparator.comparingDouble(SectionBarItem::getTotal));
        
        // 组装DTO
        SectionBarDTO dto = new SectionBarDTO();
        List<String> names = new ArrayList<>();
        List<Double> nowNums = new ArrayList<>();
        List<Double> yesterdayNums = new ArrayList<>();
        List<Double> changeAmounts = new ArrayList<>();
        
        for (SectionBarItem item : items) {
            names.add(item.getName() + "(" + String.format("%.2f", item.getIncrease()) + "%)");
            nowNums.add(round(item.getTotal() / 100000000.0));
            yesterdayNums.add(round((item.getTotal() - item.getTurnover()) / 100000000.0));
            changeAmounts.add(round(item.getTurnover() / 100000000.0));
        }
        
        dto.setCategoryName(names);
        dto.setNowNum(nowNums);
        dto.setYesterdayNum(yesterdayNums);
        dto.setChangeAmount(changeAmounts);
        
        return ApiResponseDTO.success(dto);
    }
    
    /**
     * 四舍五入到两位小数
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
    /**
     * 内部类：板块条形图数据项
     */
    private static class SectionBarItem {
        private String name;
        private double total;
        private double increase;
        private double turnover;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
        public double getIncrease() { return increase; }
        public void setIncrease(double increase) { this.increase = increase; }
        public double getTurnover() { return turnover; }
        public void setTurnover(double turnover) { this.turnover = turnover; }
    }
}
