package com.galigeigei.acloudmap.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galigeigei.acloudmap.entity.*;
import com.galigeigei.acloudmap.mapper.AInfoMapper;
import com.galigeigei.acloudmap.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${parameters.allInfo}")
    private String API_URL;
    @Resource
    private ATodayService aTodayService;

    @Resource
    private ASwDictService aSwDictService;
    @Resource
    private ASwService aSwService;
    @Resource
    private ADataJsonService aDataJsonService;

    @Override
    public ApiResult getAllInfo() {

        ADataJson todayData = aDataJsonService.getTodayData();

        if (todayData != null) {

            JSONArray jsonObject = JSONArray.parseArray(todayData.getJson());
            return ApiResult.success().data(jsonObject);
        }

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

        List<AToday> aTodayList = updateTodayInfo(jsonArray);

        JSONArray dataJson = setDataToMap(aTodayList);

        aDataJsonService.saveDataJson(dataJson);

        return ApiResult.success().data(dataJson);
    }

    /**
     * 更新今天A股数据
     *
     * @param jsonArray json数组
     * @return {@link List }<{@link AToday }>
     */
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
            aToday.setCreatTime(new Date());

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


    /**
     * 设置数据映射
     *
     * @param aTodayList 今日股票信息
     * @return {@link JSONArray }
     */
    public JSONArray setDataToMap(List<AToday> aTodayList) {
        JSONArray jsonArray = new JSONArray();

        // 获取一级分类所有code
        List<ASwDict> codeList = aSwDictService.lambdaQuery().like(ASwDict::getType, "一").list();

        // 获取一级分类所有股票
        List<ASw> alist = aSwService.lambdaQuery().like(ASw::getIndustryType, "一").list();

        // 根据code获取所有分类下的股票
        codeList.forEach(item -> {
            // 组装该分类的数据

            JSONObject jsonObject = new JSONObject();

            // 将一级分类的股票过滤出来
            List<ASw> collect = alist.stream()
                    .filter(item1 -> item1.getSwCode().equals(item.getCode()))
                    .collect(Collectors.toList());
            // 根据一级分类将股票数据过滤出来
            List<AToday> itemList = aTodayList.stream()
                    .filter(item1 -> collect.stream()
                            .map(ASw::getStockCode)
                            .collect(Collectors.toList())
                            .contains(item1.getCode()))
                    .peek(item2 -> item2.setValue(JSONArray.parseArray(item2.getArrValue())))
                    .sorted(Comparator.comparingLong(o -> ((AToday) o).getTotal()).reversed())
                    .collect(Collectors.toList());

            // 将股票一级分类需要的总市值的信息进行计算
            Long total = itemList.stream().mapToLong(AToday::getTotal).sum();

            Long[] value = {total, null, null};

            jsonObject.put("name", item.getName());
            jsonObject.put("code", item.getCode());
            jsonObject.put("total", total);
            jsonObject.put("value", value);
            jsonObject.put("children", itemList);

            jsonArray.add(jsonObject);
        });
        //根据板块总市值进行从大到小排序
        List<Object> collect = jsonArray.stream()
                .sorted(Comparator.comparingLong(o -> ((JSONObject) o).getLong("total")).reversed())
                .collect(Collectors.toList());

        return JSONArray.parseArray(JSONObject.toJSONString(collect));
    }


}
