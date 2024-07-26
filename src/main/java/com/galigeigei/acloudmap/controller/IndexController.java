package com.galigeigei.acloudmap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 周士钰
 * @date 2024/7/26 下午8:50
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "项目运行成功！"+new Date();
    }
}
