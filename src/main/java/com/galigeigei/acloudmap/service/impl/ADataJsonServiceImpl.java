package com.galigeigei.acloudmap.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galigeigei.acloudmap.entity.ADataJson;
import com.galigeigei.acloudmap.entity.model.DataBO;
import com.galigeigei.acloudmap.entity.model.SectionBO;
import com.galigeigei.acloudmap.mapper.ADataJsonMapper;
import com.galigeigei.acloudmap.service.ADataJsonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 每日数据 服务实现类
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-27
 */
@Service
public class ADataJsonServiceImpl extends ServiceImpl<ADataJsonMapper, ADataJson> implements ADataJsonService {


    @Override
    public ADataJson getTodayData() {
        return this.lambdaQuery().orderByDesc(ADataJson::getId).last("limit 1").one();
    }

    @Override
    public void saveDataJson(List<Object> aDataJson) {

        ADataJson data = this.lambdaQuery().eq(ADataJson::getToday, DateUtil.today()).oneOpt().orElseGet(ADataJson::new);

        data.setJson(JSONObject.toJSONString(aDataJson));
        data.setSection(this.assembledSection(JSONObject.toJSONString(aDataJson)));
        data.setToday(DateUtil.today());
        data.setCreatTime(new Date());

        this.saveOrUpdate(data);
    }

    /**
     * 组装二级板块数据
     *
     * @param aDataJson 数据json
     * @return {@link JSONObject }
     */
    private String assembledSection(String aDataJson) {


        ADataJson data = this.lambdaQuery().eq(ADataJson::getToday, DateUtil.yesterday().toDateStr()).oneOpt().orElseGet(ADataJson::new);

        if (data.getJson() != null) {
            // 昨日数据
            List<DataBO> yesterdayArr = JSONObject.parseObject(data.getJson(), new TypeReference<List<DataBO>>() {
            });
            // 今日数据
            List<DataBO> todayArr = JSONObject.parseObject(aDataJson, new TypeReference<List<DataBO>>() {
            });

            List<SectionBO> yesterdaySection = BeanUtil.copyToList(yesterdayArr, SectionBO.class);
            List<SectionBO> todaySection = BeanUtil.copyToList(todayArr, SectionBO.class);

            // 比较今日二级板块数据,相较于昨日的总市值差值和涨跌幅
            todaySection.forEach(section -> {

                SectionBO yesterdaySectionBO = yesterdaySection.stream().filter(s -> s.getCode().equals(section.getCode())).collect(Collectors.toList()).get(0);
                section.getChildren().forEach(children -> {

                     SectionBO.ChildrenDTO childrenDTO = yesterdaySectionBO.getChildren().stream().filter(s -> s.getCode().equals(children.getCode())).collect(Collectors.toList()).get(0);

                    children.setTurnover(children.getTotal() - childrenDTO.getTotal());
                    children.setIncrease(children.getTurnover() / childrenDTO.getTotal() * 100);
                    List<Double> value = new ArrayList<>();
                    value.add(children.getTotal());
                    value.add(children.getIncrease());
                    value.add(children.getTurnover());

                    children.setValue(value);

                });
            });

            return JSONObject.toJSONString(todaySection);
        }

        return null;

    }


}
