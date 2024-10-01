package com.galigeigei.acloudmap.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 节假日表
 * </p>
 *
 * @author 周士钰
 * @since 2024-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AHoliday implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否为节假日
     * 0:非节假日
     * 1:节假日
     */
    private Boolean holiday;

    /**
     * 节日名称
     */
    private String name;

    /**
     * 日期
     */
    private String date;


}
