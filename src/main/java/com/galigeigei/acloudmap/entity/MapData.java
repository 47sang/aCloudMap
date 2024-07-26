package com.galigeigei.acloudmap.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 云图加载json
 *
 * @author 周士钰
 * @date 2024/7/25 下午7:22
 */
@NoArgsConstructor
@Data
public class MapData {
    @JSONField(name = "errcode")
    private int errcode;
    @JSONField(name = "errmsg")
    private String errmsg;
    @JSONField(name = "data")
    private DataDTO data;

    @NoArgsConstructor
    @lombok.Data
    public static class DataDTO {
        @JSONField(name = "children")
        private List<ChildrenDTO> children;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "scale")
        private double scale;
        @JSONField(name = "id")
        private String id;

        @NoArgsConstructor
        @lombok.Data
        public static class ChildrenDTO {
            @JSONField(name = "children")
            private List<ChildrenDTO> children;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "scale")
            private double scale;
            @JSONField(name = "id")
            private String id;

            @NoArgsConstructor
            @lombok.Data
            public static class ChildrenDTO2 {
                @JSONField(name = "children")
                private List<ChildrenDTO> children;
                @JSONField(name = "name")
                private String name;
                @JSONField(name = "scale")
                private double scale;
                @JSONField(name = "id")
                private String id;

                @NoArgsConstructor
                @lombok.Data
                public static class ChildrenDTO3 {
                    @JSONField(name = "name")
                    private String name;
                    @JSONField(name = "scale")
                    private double scale;
                    @JSONField(name = "id")
                    private String id;
                }
            }
        }
    }
}
