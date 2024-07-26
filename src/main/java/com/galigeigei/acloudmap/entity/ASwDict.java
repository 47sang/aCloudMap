package com.galigeigei.acloudmap.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 申万字典
 * </p>
 *
 * @author 周士钰
 * @since 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ASwDict implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;


}
