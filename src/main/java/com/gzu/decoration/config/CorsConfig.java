
package com.gzu.decoration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域访问
     * 
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许跨域的域名
                .allowedOriginPatterns("*")
                // 允许跨域的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 是否允许携带Cookie
                .allowCredentials(true)
                // 跨域最大时间（秒）
                .maxAge(3600);
    }
}
