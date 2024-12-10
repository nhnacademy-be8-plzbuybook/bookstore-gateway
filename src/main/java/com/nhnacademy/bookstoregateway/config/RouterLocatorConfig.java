package com.nhnacademy.bookstoregateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterLocatorConfig {

    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder) {

        RouteLocator routeLocator = builder.routes().build();


        return builder.routes()
                .route("hello-service",
                        p -> p.path("/hello").and()
                                .uri("lb://HELLO-SERVICE")
                )
                .build();
    }
}

