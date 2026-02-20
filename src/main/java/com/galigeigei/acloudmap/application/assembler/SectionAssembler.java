package com.galigeigei.acloudmap.application.assembler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.galigeigei.acloudmap.application.dto.SectionDTO;
import com.galigeigei.acloudmap.domain.section.model.Section;
import com.galigeigei.acloudmap.domain.stock.model.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 板块组装器
 * 应用层：负责领域对象和DTO之间的转换
 *
 * @author DDD实践
 * @since 2024-07-26
 */
public class SectionAssembler {
    
    /**
     * 将领域对象转换为DTO
     *
     * @param section 板块领域对象
     * @return SectionDTO
     */
    public static SectionDTO toDTO(Section section) {
        if (section == null) {
            return null;
        }
        SectionDTO dto = new SectionDTO();
        dto.setCode(section.getCode().getValue());
        dto.setName(section.getName().getValue());
        
        if (section.calculateTotalMarketValue() != null) {
            dto.setTotal(section.calculateTotalMarketValue().getValue());
        }
        
        dto.setValue(List.of(section.getValueArray()));
        
        // 转换子板块或股票
        if (!section.getChildren().isEmpty()) {
            dto.setChildren(section.getChildren().stream()
                    .map(SectionAssembler::toDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setChildren(section.getStocks().stream()
                    .map(StockAssembler::toDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * 将领域对象列表转换为DTO列表
     *
     * @param sections 板块领域对象列表
     * @return SectionDTO列表
     */
    public static List<SectionDTO> toDTOList(List<Section> sections) {
        return sections.stream()
                .map(SectionAssembler::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为前端需要的JSONObject格式
     *
     * @param sections 板块列表
     * @return JSONArray
     */
    public static JSONArray toJSONArray(List<Section> sections) {
        JSONArray array = new JSONArray();
        for (Section section : sections) {
            array.add(toJSONObject(section));
        }
        return array;
    }
    
    /**
     * 将板块转换为JSONObject
     */
    private static JSONObject toJSONObject(Section section) {
        JSONObject json = new JSONObject();
        json.put("name", section.getName().getValue());
        json.put("code", section.getCode().getValue());
        
        if (section.calculateTotalMarketValue() != null) {
            json.put("total", section.calculateTotalMarketValue().getValue());
        }
        
        json.put("value", section.getValueArray());
        
        // 处理子板块
        JSONArray childrenArray = new JSONArray();
        if (!section.getChildren().isEmpty()) {
            for (Section child : section.getChildren()) {
                childrenArray.add(toChildJSONObject(child));
            }
        }
        json.put("children", childrenArray);
        
        return json;
    }
    
    /**
     * 将子板块转换为JSONObject
     */
    private static JSONObject toChildJSONObject(Section section) {
        JSONObject json = new JSONObject();
        json.put("name", section.getName().getValue());
        json.put("code", section.getCode().getValue());
        
        if (section.calculateTotalMarketValue() != null) {
            json.put("total", section.calculateTotalMarketValue().getValue());
        }
        
        json.put("value", section.getValueArray());
        
        // 处理股票
        JSONArray stocksArray = new JSONArray();
        for (Stock stock : section.getStocksSortedByMarketValue()) {
            stocksArray.add(toStockJSONObject(stock));
        }
        json.put("children", stocksArray);
        
        return json;
    }
    
    /**
     * 将股票转换为JSONObject
     */
    private static JSONObject toStockJSONObject(Stock stock) {
        JSONObject json = new JSONObject();
        json.put("code", stock.getCode().getValue());
        json.put("name", stock.getName().getValue());
        if (stock.getMarketValue() != null) {
            json.put("total", stock.getMarketValue().getValue());
        }
        if (stock.getCurrentPrice() != null) {
            json.put("price", stock.getCurrentPrice().getValue());
        }
        if (stock.getIncreaseRate() != null) {
            json.put("increase", stock.getIncreaseRate().getValue());
        }
        json.put("value", stock.getValueArray());
        json.put("today", stock.getDataDate() != null ? stock.getDataDate().toString() : "");
        return json;
    }
}
