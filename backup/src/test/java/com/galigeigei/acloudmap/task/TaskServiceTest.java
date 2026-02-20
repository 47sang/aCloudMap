package com.galigeigei.acloudmap.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author 周士钰
 * @date 2024/10/1 下午4:37
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskServiceTest {

    @Resource
    private TaskService taskService;

    @Test
    public void refreshTheStockInfo1() {
        taskService.refreshTheStockInfo1();
    }
}