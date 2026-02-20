package com.galigeigei.acloudmap.domain.stock.model;

import com.galigeigei.acloudmap.domain.shared.valueobject.*;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 股票实体
 * 充血模型：包含业务方法和领域逻辑
 * 股票是聚合根，具有唯一标识（股票代码）
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Getter
public class Stock implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 实体ID（数据库主键）
     */
    private Integer id;
    
    /**
     * 股票代码 - 实体的唯一标识
     */
    private final StockCode code;
    
    /**
     * 股票名称
     */
    private StockName name;
    
    /**
     * 当前价格
     */
    private Price currentPrice;
    
    /**
     * 总市值
     */
    private MarketValue marketValue;
    
    /**
     * 涨跌幅
     */
    private IncreaseRate increaseRate;
    
    /**
     * 换手率
     */
    private TurnoverRate turnoverRate;
    
    /**
     * 上市日期
     */
    private String listingDate;
    
    /**
     * 数据日期
     */
    private LocalDate dataDate;
    
    /**
     * 所属板块代码
     */
    private SectionCode sectionCode;
    
    /**
     * 所属二级板块代码
     */
    private SectionCode secondarySectionCode;
    
    /**
     * 私有构造函数，通过工厂方法创建
     */
    private Stock(StockCode code, StockName name) {
        this.code = code;
        this.name = name;
    }
    
    /**
     * 工厂方法：创建股票实体
     *
     * @param code 股票代码
     * @param name 股票名称
     * @return Stock 实体
     */
    public static Stock create(StockCode code, StockName name) {
        return new Stock(code, name);
    }
    
    /**
     * 工厂方法：从原始数据创建
     *
     * @param code     股票代码
     * @param name     股票名称
     * @param price    当前价格
     * @param total    总市值
     * @param increase 涨跌幅
     * @param turnover 换手率
     * @return Stock 实体
     */
    public static Stock create(String code, String name, double price, long total, 
                                double increase, double turnover) {
        Stock stock = new Stock(StockCode.of(code), StockName.of(name));
        stock.currentPrice = Price.of(price);
        stock.marketValue = MarketValue.of(total);
        stock.increaseRate = IncreaseRate.of(increase);
        stock.turnoverRate = TurnoverRate.of(turnover);
        return stock;
    }
    
    /**
     * 更新市场数据
     * 业务方法：更新股票的市场数据
     *
     * @param price    当前价格
     * @param total    总市值
     * @param increase 涨跌幅
     * @param turnover 换手率
     */
    public void updateMarketData(Price price, MarketValue total, IncreaseRate increase, TurnoverRate turnover) {
        this.currentPrice = price;
        this.marketValue = total;
        this.increaseRate = increase;
        this.turnoverRate = turnover;
    }
    
    /**
     * 设置板块归属
     * 业务方法：设置股票所属的一级和二级板块
     *
     * @param primaryCode   一级板块代码
     * @param secondaryCode 二级板块代码
     */
    public void assignToSections(SectionCode primaryCode, SectionCode secondaryCode) {
        this.sectionCode = primaryCode;
        this.secondarySectionCode = secondaryCode;
    }
    
    /**
     * 设置数据日期
     *
     * @param dataDate 数据日期
     */
    public void setDataDate(LocalDate dataDate) {
        this.dataDate = dataDate;
    }
    
    /**
     * 设置上市日期
     *
     * @param listingDate 上市日期
     */
    public void setListingDate(String listingDate) {
        this.listingDate = listingDate;
    }
    
    /**
     * 设置数据库ID
     *
     * @param id 实体ID
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * 判断是否属于指定板块
     *
     * @param sectionCode 板块代码
     * @return true 如果属于该板块
     */
    public boolean belongsToSection(SectionCode sectionCode) {
        return this.sectionCode != null && this.sectionCode.equals(sectionCode);
    }
    
    /**
     * 判断是否属于指定二级板块
     *
     * @param sectionCode 二级板块代码
     * @return true 如果属于该二级板块
     */
    public boolean belongsToSecondarySection(SectionCode sectionCode) {
        return this.secondarySectionCode != null && this.secondarySectionCode.equals(sectionCode);
    }
    
    /**
     * 获取市值数据数组（用于前端展示）
     *
     * @return 包含市值、价格、涨跌幅的数组
     */
    public long[] getValueArray() {
        return new long[]{
                marketValue != null ? marketValue.getValue() : 0L,
                currentPrice != null ? (long) (currentPrice.getValue() * 100) : 0L,
                increaseRate != null ? (long) (increaseRate.getValue() * 100) : 0L
        };
    }
    
    /**
     * 判断是否上涨
     */
    public boolean isRising() {
        return increaseRate != null && increaseRate.isRising();
    }
    
    /**
     * 判断是否下跌
     */
    public boolean isFalling() {
        return increaseRate != null && increaseRate.isFalling();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return code.equals(stock.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    @Override
    public String toString() {
        return "Stock{" +
                "code=" + code +
                ", name=" + name +
                ", price=" + currentPrice +
                ", marketValue=" + marketValue +
                ", increase=" + increaseRate +
                '}';
    }
}
