package com.galigeigei.acloudmap.service;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.galigeigei.acloudmap.entity.ADataJson;

/**
 * <p>
 * 每日数据 服务类
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-27
 */
public interface ADataJsonService extends IService<ADataJson> {

    /**
     * 获取今日数据json
     *
     * @return boolean
     */
    ADataJson getTodayData();

    /**
     * 保存每日数据json
     *
     * @param aDataJson 数据json
     * @return boolean
     */
    boolean saveDataJson(JSONArray aDataJson);
}
