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
        System.out.println("项目启动完成: http://localhost:1808/aCloudMap/");
    }


}
