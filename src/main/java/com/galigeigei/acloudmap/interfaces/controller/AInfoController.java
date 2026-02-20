package com.galigeigei.acloudmap.interfaces.controller;

import com.alibaba.fastjson2.JSONArray;
import com.galigeigei.acloudmap.application.dto.ApiResponseDTO;
import com.galigeigei.acloudmap.application.dto.SectionBarDTO;
import com.galigeigei.acloudmap.application.dto.StockDTO;
import com.galigeigei.acloudmap.application.service.StockQueryApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 股票信息接口控制器
 * 接口层：处理HTTP请求，调用应用服务
 * 遵循DDD的分层架构，不包含业务逻辑
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@RestController
@RequestMapping("/info")
public class AInfoController {

    private final StockQueryApplicationService stockQueryApplicationService;

    public AInfoController(StockQueryApplicationService stockQueryApplicationService) {
        this.stockQueryApplicationService = stockQueryApplicationService;
    }

    /**
     * 获取所有股票信息（大盘云图数据）
     *
     * @return 按板块聚合的股票数据
     */
    @GetMapping("/all")
    public ApiResponseDTO<JSONArray> getAllInfo() {
        return stockQueryApplicationService.getAllStockInfo();
    }

    /**
     * 获取按市值排序的股票信息
     *
     * @return 按市值降序排列的股票列表
     */
    @GetMapping("/sort")
    public ApiResponseDTO<List<StockDTO>> getSortInfo() {
        return stockQueryApplicationService.getStocksSortedByMarketValue();
    }

    /**
     * 获取板块信息
     *
     * @return 板块数据
     */
    @GetMapping("/section")
    public ApiResponseDTO<JSONArray> getSection() {
        return stockQueryApplicationService.getSectionInfo();
    }

    /**
     * 获取板块条形图数据
     *
     * @return 板块涨跌条形图数据
     */
    @GetMapping("/sectionBar")
    public ApiResponseDTO<SectionBarDTO> getSectionBar() {
        return stockQueryApplicationService.getSectionBarData();
    }
}
