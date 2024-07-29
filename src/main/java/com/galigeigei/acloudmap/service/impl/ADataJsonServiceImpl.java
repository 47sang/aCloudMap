package com.galigeigei.acloudmap.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galigeigei.acloudmap.entity.ADataJson;
import com.galigeigei.acloudmap.mapper.ADataJsonMapper;
import com.galigeigei.acloudmap.service.ADataJsonService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        return this.lambdaQuery().eq(ADataJson::getToday, DateUtil.today()).one();
    }

    @Override
    public void saveDataJson(List<Object> aDataJson) {

        ADataJson data = this.lambdaQuery().eq(ADataJson::getToday, DateUtil.today()).list().stream().findFirst().orElseGet(ADataJson::new);

        data.setJson(JSONObject.toJSONString(aDataJson));
        data.setToday(DateUtil.today());
        data.setCreatTime(new Date());

        this.saveOrUpdate(data);
    }
}
