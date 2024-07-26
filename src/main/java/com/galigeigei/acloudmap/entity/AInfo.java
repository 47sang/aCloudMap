package com.galigeigei.acloudmap.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 股票基础信息板块
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AInfo implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 板块id
     */
    private String bkId;

    /**
     * 板块名称
     */
    private String bkName;

    /**
     * 二级板块
     */
    private String ejId;

    /**
     * 二级板块名称
     */
    private String ejName;

    /**
     * 所属交易所
     */
    private String exchange;


}
