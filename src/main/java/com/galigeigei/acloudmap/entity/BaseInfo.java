package com.galigeigei.acloudmap.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 股票基础信息
 *
 * @author 周士钰
 * @date 2024/7/26 上午11:13
 */
@NoArgsConstructor
@Data
public class BaseInfo {
    //有用的数据:f2,f3,f8,f12,f14,f20,f26
    //所有数据:f2,f3,f4,f5,f6,f7,f8,f10,f12,f14,f15,f16,f17,f18,f20,f21,f23,f26
    @JSONField(name = "f1")
    private int f1;
    /**
     * 最新价
     */
    @JSONField(name = "f2")
    private double f2;
    /**
     * 涨跌幅
     */
    @JSONField(name = "f3")
    private double f3;
    /**
     * 涨跌额
     */
    @JSONField(name = "f4")
    private double f4;
    /**
     * 成交量(手)
     */
    @JSONField(name = "f5")
    private int f5;
    /**
     * 成交额(元)
     */
    @JSONField(name = "f6")
    private double f6;
    /**
     * 振幅
     */
    @JSONField(name = "f7")
    private double f7;
    /**
     * 换手率(%)
     */
    @JSONField(name = "f8")
    private double f8;
    /**
     * 市盈率(动)
     */
    @JSONField(name = "f9")
    private double f9;
    /**
     * 量比
     */
    @JSONField(name = "f10")
    private double f10;
    @JSONField(name = "f11")
    private double f11;
    /**
     * 股票代码
     */
    @JSONField(name = "f12")
    private String f12;
    @JSONField(name = "f13")
    private int f13;
    /**
     * 股票名称
     */
    @JSONField(name = "f14")
    private String f14;
    /**
     * 最高价(元)
     */
    @JSONField(name = "f15")
    private double f15;
    /**
     * 最低价(元)
     */
    @JSONField(name = "f16")
    private double f16;
    /**
     * 开盘价(元)
     */
    @JSONField(name = "f17")
    private double f17;
    /**
     * 昨日收盘价(元)
     */
    @JSONField(name = "f18")
    private double f18;
    @JSONField(name = "f19")
    private int f19;
    /**
     * 总市值(元)
     */
    @JSONField(name = "f20")
    private long f20;
    /**
     * 流通市值(元)
     */
    @JSONField(name = "f21")
    private double f21;
    @JSONField(name = "f22")
    private double f22;
    /**
     * 市净率
     */
    @JSONField(name = "f23")
    private double f23;
    @JSONField(name = "f24")
    private double f24;
    @JSONField(name = "f25")
    private double f25;
    /**
     * 上市日期:20240726
     */
    @JSONField(name = "f26")
    private String f26;
    @JSONField(name = "f27")
    private int f27;
    @JSONField(name = "f28")
    private String f28;
    @JSONField(name = "f29")
    private int f29;
}
