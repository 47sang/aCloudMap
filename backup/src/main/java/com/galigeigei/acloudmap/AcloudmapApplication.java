package com.galigeigei.acloudmap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.galigeigei.acloudmap.mapper")
public class AcloudmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcloudmapApplication.class, args);
        System.out.println("大盘云图: http://localhost:1808/aCloudMap/");
        System.out.println("市值大小排序: http://localhost:1808/aCloudMap/sort");
        System.out.println("二级板块云图: http://localhost:1808/aCloudMap/section");
        System.out.println("二级板块条形图: http://localhost:1808/aCloudMap/sectionBar");
    }


}
