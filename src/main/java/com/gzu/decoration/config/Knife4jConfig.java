package com.gzu.decoration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j/Swagger API文档配置
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("装修工人接单管理系统 API文档")
                        .version("1.0")
                        .description("装修工人接单管理系统 RESTful API接口文档")
                        .contact(new Contact()
                                .name("Thouser-wu")
                                .email("example@example.com")));
    }

    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .displayName("全部接口")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .displayName("认证模块")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("customer")
                .displayName("客户管理")
                .pathsToMatch("/api/customer/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("order")
                .displayName("订单管理")
                .pathsToMatch("/api/order/**")
                .build();
    }

    @Bean
    public GroupedOpenApi materialApi() {
        return GroupedOpenApi.builder()
                .group("material")
                .displayName("材料管理")
                .pathsToMatch("/api/material/**")
                .build();
    }
}
