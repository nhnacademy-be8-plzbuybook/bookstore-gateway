package com.nhnacademy.bookstoregateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator msRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service", r -> r.path("/auth/**")  // /auth/** 경로 처리
                        .uri("lb://auth-service"))             // Eureka를 통한 동적 라우팅
                .build();
    }
}