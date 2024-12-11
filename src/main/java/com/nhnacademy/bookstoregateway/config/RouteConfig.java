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
                .route("example_route", r -> r.path("/example/**")
                        .uri("http://localhost:8080"))
                .build();
    }
}