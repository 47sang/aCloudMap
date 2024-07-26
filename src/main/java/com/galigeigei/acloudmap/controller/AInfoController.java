package com.galigeigei.acloudmap.controller;


import com.galigeigei.acloudmap.entity.ApiResult;
import com.galigeigei.acloudmap.service.AInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 股票基础信息板块 前端控制器
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-26
 */
@RestController
@RequestMapping("/info")
public class AInfoController {

    @Resource
    private AInfoService aInfoService;

    @GetMapping("/all")
    public ApiResult getAllInfo(){
        return aInfoService.getAllInfo();
    }
}

