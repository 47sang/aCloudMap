package com.galigeigei.acloudmap.domain.section.model;

import com.galigeigei.acloudmap.domain.shared.valueobject.*;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 板块实体
 * 充血模型：包含业务方法和领域逻辑
 * 板块是聚合根，包含该板块下的所有股票
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public class Section implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 实体ID
     */
    private Integer id;
    
    /**
     * 板块代码 - 实体的唯一标识
     */
    private final SectionCode code;
    
    /**
     * 板块名称
     */
    private final SectionName name;
    
    /**
     * 板块级别
     */
    private final SectionCode.SectionLevel level;
    
    /**
     * 父板块代码（二级板块使用）
     */
    private SectionCode parentCode;
    
    /**
     * 板块下的股票列表
     */
    private final List<Stock> stocks;
    
    /**
     * 子板块列表（一级板块使用）
     */
    private final List<Section> children;
    
    /**
     * 私有构造函数
     */
    private Section(SectionCode code, SectionName name, SectionCode.SectionLevel level) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.stocks = new ArrayList<>();
        this.children = new ArrayList<>();
    }
    
    /**
     * 工厂方法：创建一级板块
     *
     * @param code 板块代码
     * @param name 板块名称
     * @return Section 实体
     */
    public static Section createPrimary(String code, String name) {
        return new Section(SectionCode.of(code, SectionCode.SectionLevel.PRIMARY), 
                          SectionName.of(name), 
                          SectionCode.SectionLevel.PRIMARY);
    }
    
    /**
     * 工厂方法：创建二级板块
     *
     * @param code       板块代码
     * @param name       板块名称
     * @param parentCode 父板块代码
     * @return Section 实体
     */
    public static Section createSecondary(String code, String name, String parentCode) {
        Section section = new Section(SectionCode.of(code, SectionCode.SectionLevel.SECONDARY), 
                                      SectionName.of(name), 
                                      SectionCode.SectionLevel.SECONDARY);
        section.parentCode = SectionCode.of(parentCode);
        return section;
    }
    
    /**
     * 添加股票到板块
     * 业务方法
     *
     * @param stock 股票实体
     */
    public void addStock(Stock stock) {
        if (stock != null && !stocks.contains(stock)) {
            stocks.add(stock);
        }
    }
    
    /**
     * 添加子板块
     * 业务方法：仅一级板块可使用
     *
     * @param child 子板块
     */
    public void addChildSection(Section child) {
        if (this.level == SectionCode.SectionLevel.PRIMARY && child != null) {
            this.children.add(child);
        }
    }
    
    /**
     * 计算板块总市值
     * 业务方法：汇总板块下所有股票的市值
     *
     * @return 板块总市值
     */
    public MarketValue calculateTotalMarketValue() {
        long total = stocks.stream()
                .mapToLong(stock -> stock.getMarketValue() != null ? stock.getMarketValue().getValue() : 0L)
                .sum();
        return MarketValue.of(total);
    }
    
    /**
     * 计算板块平均涨跌幅
     * 业务方法：按市值加权计算
     *
     * @return 加权平均涨跌幅
     */
    public IncreaseRate calculateWeightedIncreaseRate() {
        double totalValue = 0;
        double weightedIncrease = 0;
        
        for (Stock stock : stocks) {
            if (stock.getMarketValue() != null && stock.getIncreaseRate() != null) {
                double value = stock.getMarketValue().getValue();
                totalValue += value;
                weightedIncrease += value * stock.getIncreaseRate().getValue();
            }
        }
        
        if (totalValue == 0) {
            return IncreaseRate.of(0);
        }
        
        return IncreaseRate.of(weightedIncrease / totalValue);
    }
    
    /**
     * 计算涨跌额
     * 业务方法：当前市值 - 昨日市值
     *
     * @param yesterdayValue 昨日市值
     * @return 涨跌额
     */
    public double calculateTurnover(MarketValue yesterdayValue) {
        MarketValue todayValue = calculateTotalMarketValue();
        return todayValue.subtract(yesterdayValue);
    }
    
    /**
     * 按市值排序股票
     * 业务方法
     *
     * @return 按市值降序排列的股票列表
     */
    public List<Stock> getStocksSortedByMarketValue() {
        return stocks.stream()
                .sorted(Comparator.comparing(Stock::getMarketValue, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
    
    /**
     * 获取股票数量
     */
    public int getStockCount() {
        return stocks.size();
    }
    
    /**
     * 判断是否包含指定股票
     */
    public boolean containsStock(StockCode stockCode) {
        return stocks.stream().anyMatch(stock -> stock.getCode().equals(stockCode));
    }
    
    /**
     * 设置数据库ID
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * 获取板块数据值数组（用于前端展示）
     *
     * @return 包含总市值的数组
     */
    public Long[] getValueArray() {
        MarketValue total = calculateTotalMarketValue();
        return new Long[]{total.getValue(), null, null};
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return code.equals(section.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    @Override
    public String toString() {
        return "Section{" +
                "code=" + code +
                ", name=" + name +
                ", level=" + level +
                ", stockCount=" + stocks.size() +
                '}';
    }
}
