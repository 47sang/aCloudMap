package com.galigeigei.acloudmap.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 * 申万板块
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-26
 */
@Data
public class ASw {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 申万板块代码
     */
    private String swCode;

    /**
     * 板块名称
     */
    private String industryName;

    /**
     * 申万类型
     */
    private String industryType;

    /**
     * 来源
     */
    private String source;


}
