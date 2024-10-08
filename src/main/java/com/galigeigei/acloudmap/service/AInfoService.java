package com.galigeigei.acloudmap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.galigeigei.acloudmap.entity.AInfo;
import com.galigeigei.acloudmap.entity.ApiResult;

/**
 * <p>
 * 股票基础信息板块 服务类
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-26
 */
public interface AInfoService extends IService<AInfo> {

    /**
     * 获取一二级分类整理后的所有股票信息
     *
     * @return {@link ApiResult }
     */
    ApiResult getAllInfo();

    /**
     * 获取今日股票信息
     *
     * @return {@link ApiResult }
     */
    ApiResult getTodayInfo();

    /**
     * 按照市值排序的股票信息
     *
     * @return {@link ApiResult }
     */
    ApiResult getSortInfo();


    /**
     * 获取板块涨跌信息
     *
     * @return {@link ApiResult }
     */
    ApiResult getSection();


    /**
     * 获取二级板块正负条形图数据
     *
     * @return {@link ApiResult }
     */
    ApiResult getSectionBar();
}
