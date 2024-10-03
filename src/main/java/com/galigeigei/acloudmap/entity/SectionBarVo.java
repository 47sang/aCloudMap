package com.galigeigei.acloudmap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 二级板块正负条形图数据
 * @author 周士钰
 * @date 2024/10/3 下午5:08
 */
@Data
@Accessors(chain = true)
public class SectionBarVo {

    /**
     * 分类名称
     */
    private List<String> categoryName;
    /**
     * 当前市值
     */
    private List<Double> nowNum;
    /**
     * 昨日市值
     */
    private List<Double> yesterdayNum;
    /**
     * 涨跌额
     */
    private List<Double> changeAmount;

}
