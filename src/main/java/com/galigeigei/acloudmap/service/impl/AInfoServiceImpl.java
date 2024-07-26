package com.galigeigei.acloudmap.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galigeigei.acloudmap.entity.AInfo;
import com.galigeigei.acloudmap.entity.AToday;
import com.galigeigei.acloudmap.entity.ApiResult;
import com.galigeigei.acloudmap.entity.BaseInfo;
import com.galigeigei.acloudmap.mapper.AInfoMapper;
import com.galigeigei.acloudmap.service.AInfoService;
import com.galigeigei.acloudmap.service.ATodayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Resource
    private ATodayService aTodayService;

    @Override
    public ApiResult getAllInfo() {

        Map<String, Object> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "7000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");
        params.put("fields", "f2,f3,f8,f12,f14,f20,f26");
        params.put("_", "1623833739532");

        String resultStr = HttpUtil.get(API_URL, params);


        JSONArray jsonArray = JSONObject.parseObject(resultStr).getJSONObject("data").getJSONArray("diff");

        return ApiResult.success().data(updateTodayInfo(jsonArray));
    }

    public List<AToday> updateTodayInfo(JSONArray jsonArray) {
        List<AToday> aTodayList = aTodayService.lambdaQuery().eq(AToday::getToday, DateUtil.today()).list();

        List<AToday> newList = new ArrayList<>();

        jsonArray.forEach(item -> {
            BaseInfo baseInfo = JSONObject.parseObject(JSONObject.toJSONString(item), BaseInfo.class);

            AToday aToday = aTodayList.stream().filter(item1 -> item1.getCode().equals(baseInfo.getF12())).findFirst().orElseGet(AToday::new);

            aToday.setCode(baseInfo.getF12());
            aToday.setName(baseInfo.getF14());
            aToday.setTotal(baseInfo.getF20());
            aToday.setPrice(baseInfo.getF2());
            aToday.setIncrease(baseInfo.getF3());
            aToday.setTurnover(baseInfo.getF8());
            aToday.setIntoDate(baseInfo.getF26());
            aToday.setToday(DateUtil.today());

            JSONArray arr = new JSONArray();
            arr.add(aToday.getTotal());
            arr.add(aToday.getPrice());
            arr.add(aToday.getIncrease());

            aToday.setArrValue(arr.toJSONString());
            newList.add(aToday);
        });

        aTodayService.saveOrUpdateBatch(newList);

        return newList;

    }


}
