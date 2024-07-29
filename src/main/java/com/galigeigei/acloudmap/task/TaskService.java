package com.galigeigei.acloudmap.task;

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
    @Scheduled(cron = "0 16-59/1 9 * * 1-5")
    public void refreshTheStockInfo1() {
        long startTime = System.currentTimeMillis();

        aInfoService.getTodayInfo();

        long time = System.currentTimeMillis() - startTime;
        log.info("本次定时任务执行耗时:{}秒", time/1000);
    }

    @Scheduled(cron = "0 */1 10-11 * * 1-5")
    public void refreshTheStockInfo2() {
        aInfoService.getTodayInfo();
    }

    @Scheduled(cron = "0 0-30/1 11 * * 1-5")
    public void refreshTheStockInfo3() {
        aInfoService.getTodayInfo();
    }

    // 下午开盘期间
    @Scheduled(cron = "0 0-59/1 13-15 * * 1-5")
    public void refreshTheStockInfo4() {
        aInfoService.getTodayInfo();
    }
}
