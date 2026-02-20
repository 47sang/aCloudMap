package com.galigeigei.acloudmap.infrastructure.external;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.AHolidayEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 节假日API客户端
 * 基础设施层：负责与外部节假日服务通信
 *
 * @author DDD实践
 * @since 2024-10-01
 */
@Slf4j
@Service
public class HolidayApiClient {
    
    @Value("${parameters.holidayUrl}")
    private String holidayApiUrl;
    
    /**
     * 查询指定日期是否为节假日
     *
     * @param date 日期字符串 (yyyy-MM-dd)
     * @return 节假日信息
     */
    public AHolidayEntity fetchHolidayInfo(String date) {
        try {
            String apiUrl = holidayApiUrl + date;
            String result = HttpUtil.get(apiUrl);
            JSONObject jsonObject = JSONObject.parseObject(result);
            
            JSONObject holiday = jsonObject.getJSONObject("holiday");
            AHolidayEntity entity = new AHolidayEntity();
            entity.setDate(date);
            
            if (holiday != null) {
                entity.setName(holiday.getString("name"));
                entity.setHoliday(holiday.getBoolean("holiday"));
            } else {
                entity.setHoliday(false);
            }
            
            return entity;
            
        } catch (Exception e) {
            log.error("获取节假日信息失败: {}", date, e);
            // 默认非节假日
            AHolidayEntity entity = new AHolidayEntity();
            entity.setDate(date);
            entity.setHoliday(false);
            return entity;
        }
    }
}
