package com.galigeigei.acloudmap.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 每日数据
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ADataJson implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据
     */
    private String json;

    /**
     * 数据日期
     */
    private String today;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;


}
