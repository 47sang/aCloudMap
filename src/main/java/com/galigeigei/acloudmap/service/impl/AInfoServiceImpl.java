package com.galigeigei.acloudmap.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galigeigei.acloudmap.entity.AInfo;
import com.galigeigei.acloudmap.entity.ApiResult;
import com.galigeigei.acloudmap.mapper.AInfoMapper;
import com.galigeigei.acloudmap.service.AInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 股票基础信息板块 服务实现类
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-26
 */
@Service
public class AInfoServiceImpl extends ServiceImpl<AInfoMapper, AInfo> implements AInfoService {

    static final String API_URL = "https://82.push2.eastmoney.com/api/qt/clist/get";

    @Override
    public ApiResult getAllInfo() {

        Map<String,Object> params = new HashMap<>();
        params.put("pn","1");
        params.put("pz","7000");
        params.put("po","1");
        params.put("np","1");
        params.put("ut","bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt","2");
        params.put("invt","2");
        params.put("fid","f3");
        params.put("fs","m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");
        params.put("fields","f2,f3,f8,f12,f14,f20,f26");
        params.put("_","1623833739532");

        String resultStr = HttpUtil.get(API_URL, params);


        JSONArray jsonArray = JSONObject.parseObject(resultStr).getJSONObject("data").getJSONArray("diff");

        return ApiResult.success().data(jsonArray);
    }


}
