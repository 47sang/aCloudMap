package com.galigeigei.acloudmap.infrastructure.external;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.galigeigei.acloudmap.domain.stock.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 东方财富外部API客户端
 * 基础设施层：负责与外部服务通信
 * 获取实时市场数据
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Slf4j
@Service
public class EastMoneyApiClient {
    
    @Value("${parameters.allInfo}")
    private String apiUrl;
    
    /**
     * 获取今日市场数据
     *
     * @return 股票列表
     */
    public List<Stock> fetchTodayMarketData() {
        Map<String, Object> params = buildRequestParams();
        
        try {
            String resultStr = HttpUtil.get(apiUrl, params);
            String replace = resultStr.replace("\"-\"", "0");
            
            JSONObject dataObj = JSONObject.parseObject(replace).getJSONObject("data");
            if (dataObj == null) {
                log.error("API返回数据为空");
                return new ArrayList<>();
            }
            
            JSONArray jsonArray = dataObj.getJSONArray("diff");
            if (jsonArray == null) {
                log.error("API返回diff数据为空");
                return new ArrayList<>();
            }
            
            return parseStockData(jsonArray);
            
        } catch (Exception e) {
            log.error("获取市场数据失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 按市值排序获取市场数据
     *
     * @return 按市值排序的股票列表
     */
    public List<Stock> fetchMarketDataSortedByMarketValue() {
        Map<String, Object> params = buildRequestParams();
        params.put("fid", "f20"); // 按市值排序
        
        try {
            String resultStr = HttpUtil.get(apiUrl, params);
            String replace = resultStr.replace("\"-\"", "0");
            
            JSONArray jsonArray = JSONObject.parseObject(replace).getJSONObject("data").getJSONArray("diff");
            return parseStockData(jsonArray);
            
        } catch (Exception e) {
            log.error("获取排序市场数据失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 构建请求参数
     */
    private Map<String, Object> buildRequestParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "7000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3"); // 默认按涨跌幅排序
        params.put("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");
        params.put("fields", "f2,f3,f8,f12,f14,f20,f26");
        params.put("_", "1623833739532");
        return params;
    }
    
    /**
     * 解析股票数据
     * 字段说明：
     * f2: 最新价
     * f3: 涨跌幅
     * f8: 换手率
     * f12: 股票代码
     * f14: 股票名称
     * f20: 总市值
     * f26: 上市日期
     */
    private List<Stock> parseStockData(JSONArray jsonArray) {
        List<Stock> stocks = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            
            String code = item.getString("f12");
            String name = item.getString("f14");
            double price = item.getDoubleValue("f2");
            double increase = item.getDoubleValue("f3");
            double turnover = item.getDoubleValue("f8");
            long total = item.getLongValue("f20");
            String listingDate = item.getString("f26");
            
            if (code != null && name != null) {
                Stock stock = Stock.create(code, name, price, total, increase, turnover);
                stock.setListingDate(listingDate);
                stocks.add(stock);
            }
        }
        
        return stocks;
    }
}
