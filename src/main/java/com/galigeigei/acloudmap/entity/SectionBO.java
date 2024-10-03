package com.galigeigei.acloudmap.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 二级板块涨跌数据
 * @author 周士钰
 * @date 2024/9/30 下午9:06
 */
@NoArgsConstructor
@Data
public class SectionBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * code
     */
    private String code;
    /**
     * name
     */
    private String name;
    /**
     * total
     */
    private long total;
    /**
     * value
     */
    private List<Long> value;
    /**
     * children
     */
    private List<ChildrenDTO> children;

    /**
     * ChildrenDTO
     */
    @NoArgsConstructor
    @Data
    public static class ChildrenDTO {
        /**
         * code
         */
        private String code;
        /**
         * name
         */
        private String name;
        /**
         * 当前市值
         */
        private double total;
        /**
         * 涨跌幅%
         */
        private double increase;
        /**
         * 涨跌额
         */
        private double turnover;
        /**
         * value
         */
        private List<Double> value;
    }

}
