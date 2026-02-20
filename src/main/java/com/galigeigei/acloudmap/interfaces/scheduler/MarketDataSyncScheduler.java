package com.galigeigei.acloudmap.interfaces.scheduler;

import cn.hutool.core.date.DateUtil;
import com.galigeigei.acloudmap.application.service.MarketDataApplicationService;
import com.galigeigei.acloudmap.domain.stock.event.MarketDataSyncedEvent;
import com.galigeigei.acloudmap.infrastructure.external.HolidayApiClient;
import com.galigeigei.acloudmap.infrastructure.mapper.AHolidayMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.AHolidayEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * 定时任务调度器
 * 接口层：负责定时触发业务逻辑
 * 在开盘期间自动刷新市场数据
 *
 * @author DDD实践
 * @date 2024/7/29
 */
@Slf4j
@Component
public class MarketDataSyncScheduler {

    private final MarketDataApplicationService marketDataApplicationService;
    private final HolidayApiClient holidayApiClient;
    private final AHolidayMapper aHolidayMapper;

    public MarketDataSyncScheduler(MarketDataApplicationService marketDataApplicationService,
                                   HolidayApiClient holidayApiClient,
                                   AHolidayMapper aHolidayMapper) {
        this.marketDataApplicationService = marketDataApplicationService;
        this.holidayApiClient = holidayApiClient;
        this.aHolidayMapper = aHolidayMapper;
    }

    /**
     * 开盘期间定时刷新数据
     * 上午 9:30-11:30，下午 13:00-15:00
     * 每1分钟执行一次
     */
    @Scheduled(cron = "2 0-59/1 9-15 * * 1-5")
    public void refreshMarketData() {
        // 判断今天是否为节假日
        String today = DateUtil.today();
        Optional<AHolidayEntity> holidayOpt = Optional.ofNullable(aHolidayMapper.selectByDate(today));
        
        if (holidayOpt.isPresent()) {
            if (holidayOpt.get().getHoliday()) {
                log.info("今天是节假日，跳过数据同步");
                return;
            }
        } else {
            // 查询节假日API
            AHolidayEntity holidayEntity = holidayApiClient.fetchHolidayInfo(today);
            holidayEntity.setCreatTime(new Date());
            aHolidayMapper.insert(holidayEntity);
            
            if (holidayEntity.getHoliday()) {
                log.info("今天是节假日，跳过数据同步");
                return;
            }
        }

        // 判断当前时间是否在开盘期间
        if (isInTradingHours()) {
            long startTime = System.currentTimeMillis();
            
            MarketDataSyncedEvent event = marketDataApplicationService.syncTodayMarketData();
            
            long time = System.currentTimeMillis() - startTime;
            
            if (event.isSuccess()) {
                log.info("定时任务完成，刷新最新数据耗时：{}秒，同步股票数量：{}", 
                        time / 1000, event.getStockCount());
            } else {
                log.error("定时任务失败：{}", event.getMessage());
            }
        }
    }
    
    /**
     * 判断当前是否在交易时间
     */
    private boolean isInTradingHours() {
        return DateUtil.isIn(DateUtil.date(), 
                DateUtil.parse("09:15:00"), 
                DateUtil.parse("11:30:05"))
                || DateUtil.isIn(DateUtil.date(), 
                        DateUtil.parse("13:00:00"), 
                        DateUtil.parse("15:00:05"));
    }
}
