package com.galigeigei.acloudmap.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.galigeigei.acloudmap.entity.AInfo;
import com.galigeigei.acloudmap.entity.ASw;
import com.galigeigei.acloudmap.entity.BaseInfo;
import com.galigeigei.acloudmap.service.AInfoService;
import com.galigeigei.acloudmap.service.ASwService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author 周士钰
 * @date 2024/7/26 下午11:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AInfoServiceImplTest {

    static final String API_URL = "https://82.push2.eastmoney.com/api/qt/clist/get";

    @Resource
    private ASwService aSwService;

    @Resource
    private AInfoService aInfoService;

    /**
     * 从接口获取的股票信息,和申万字典中的数据进行匹配,
     * 然后保存到info表中,供大图分类时使用
     */
    @Test
    public void addAllInfo() {
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

        List<AInfo> aInfoList = aInfoService.list();

        List<ASw> list = aSwService.list();

        jsonArray.forEach(item -> {
            BaseInfo json = JSONObject.parseObject(JSONObject.toJSONString(item), BaseInfo.class);

            Optional<ASw> one = list.stream().filter(aSw -> aSw.getStockCode().equals(json.getF12()))
                    .filter(aSw -> aSw.getIndustryType().contains("一")).findFirst();

            Optional<ASw> two = list.stream().filter(aSw -> aSw.getStockCode().equals(json.getF12()))
                    .filter(aSw -> aSw.getIndustryType().contains("二")).findFirst();


            AInfo aInfo = aInfoList.stream().filter(aInfo1 -> aInfo1.getCode().equals(json.getF12())).findFirst().orElseGet(AInfo::new);
            aInfo.setCode(json.getF12())
                    .setName(json.getF14())
                    .setExchange(exchange(json.getF12()))
                    .setBkId(one.isPresent() ? one.get().getSwCode() : "")
                    .setBkName(one.isPresent() ? one.get().getIndustryName() : "")
                    .setEjId(two.isPresent() ? two.get().getSwCode() : "")
                    .setEjName(two.isPresent() ? two.get().getIndustryName() : "");

            aInfoList.add(aInfo);
        });

        aInfoList.sort(Comparator.comparing(AInfo::getCode));

        boolean b = aInfoService.saveOrUpdateBatch(aInfoList);

        System.out.println(b ? "批量新增成功" : "失败");

        Assert.assertTrue(b);

    }

    /**
     * 根据股票代码判断属于哪个交易所
     *
     * @param code 代码
     * @return {@link String }
     */
    public String exchange(String code) {
        /*
        '0': 'SZ',
        '3': 'SZ',
        '6': 'SH',
        '9': 'SH',
        '4': 'BJ',
        '8': 'BJ'
         */
        if (code.startsWith("0") || code.startsWith("3")) {
            return "SZ";
        } else if (code.startsWith("6") || code.startsWith("9")) {
            return "SH";
        } else if (code.startsWith("4") || code.startsWith("8")) {
            return "BJ";
        }
        return "";
    }
}