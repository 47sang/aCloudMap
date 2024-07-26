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
     * 获取所有股票信息
     *
     * @return {@link ApiResult }
     */
    ApiResult getAllInfo();
}
