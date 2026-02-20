package com.galigeigei.acloudmap.interfaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面请求控制器
 * 接口层：处理页面跳转请求
 *
 * @author DDD实践
 * @date 2024/7/26
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String html() {
        return "index.html";
    }

    @RequestMapping("/sort")
    public String sort() {
        return "sort.html";
    }

    @RequestMapping("/section")
    public String section() {
        return "section.html";
    }

    @RequestMapping("/sectionBar")
    public String sectionBar() {
        return "sectionBar.html";
    }
}
