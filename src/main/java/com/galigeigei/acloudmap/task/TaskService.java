package com.galigeigei.acloudmap.task;

import cn.hutool.core.date.DateUtil;
import com.galigeigei.acloudmap.service.AInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务
 *
 * @author 周士钰
 * @date 2024/7/29 上午8:37
 */
@Slf4j
@Component
public class TaskService {

    @Resource
    private AInfoService aInfoService;

    // 上午开盘期间
    @Scheduled(cron = "2 0-59/1 9-15 * * 1-5")
    public void refreshTheStockInfo1() {
        timeLog();
    }

    /**
     * 记录本次定时任务执行耗时
     */
    public void timeLog() {

        // 判断当前时间是否在开盘期间
        if (DateUtil.isIn(DateUtil.date(), DateUtil.parse("09:15:00"), DateUtil.parse("11:30:05"))
                || DateUtil.isIn(DateUtil.date(), DateUtil.parse("13:00:00"), DateUtil.parse("15:00:05"))) {

            long startTime = System.currentTimeMillis();

            aInfoService.getTodayInfo();

            long time = System.currentTimeMillis() - startTime;
            log.info("定时任务,刷新最新数据耗时:{}秒", time / 1000);
        }
    }
}
