package com.gzu.decoration.config;

import com.gzu.decoration.interceptor.JwtAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类 - 配置拦截器
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    /**
     * 添加拦截器
     * 
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                // 拦截所有请求
                .addPathPatterns("/api/**")
                // 排除不需要认证的接口
                .excludePathPatterns(
                        "/api/auth/login",      // 登录接口
                        "/api/auth/register",    // 注册接口（如果有）
                        "/error",                // 错误页面
                        "/static/**"             // 静态资源
                );
    }
}
