package com.galigeigei.acloudmap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面请求
 * @author 周士钰
 * @date 2024/7/26 下午8:50
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
}
