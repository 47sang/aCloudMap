package com.galigeigei.acloudmap.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域访问配置
 * 基础设施层：Web相关配置
 *
 * @author DDD实践
 * @date 2024/7/30
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://47sang.github.io")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
