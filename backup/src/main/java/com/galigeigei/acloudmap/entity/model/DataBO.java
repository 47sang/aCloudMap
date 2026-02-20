package com.galigeigei.acloudmap.entity.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 大盘云图个股涨跌数据
 * @author 周士钰
 * @date 2024/9/30 下午9:06
 */
@NoArgsConstructor
@Data
public class DataBO implements Serializable {

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
        private List<ChildrenDTO3> children;

        /**
         * ChildrenDTO
         */
        @NoArgsConstructor
        @Data
        public static class ChildrenDTO3 {
            /**
             * id
             */
            private int id;
            /**
             * code
             */
            private String code;
            /**
             * name
             */
            private String name;
            /**
             * price
             */
            private double price;
            /**
             * today
             */
            private String today;
            /**
             * total
             */
            private long total;
            /**
             * value
             */
            private List<Long> value;
            /**
             * arrValue
             */
            private String arrValue;
            /**
             * increase
             */
            private double increase;
            /**
             * intoDate
             */
            private String intoDate;
            /**
             * turnover
             */
            private double turnover;
            /**
             * creatTime
             */
            private String creatTime;
        }
    }

}
