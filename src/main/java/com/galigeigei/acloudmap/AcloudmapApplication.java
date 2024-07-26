package com.galigeigei.acloudmap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.galigeigei.acloudmap.mapper")
public class AcloudmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcloudmapApplication.class, args);
    }


}
