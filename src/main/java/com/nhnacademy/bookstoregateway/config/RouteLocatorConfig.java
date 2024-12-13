package com.nhnacademy.bookstoregateway.config;

import com.nhnacademy.bookstoregateway.filter.JwtAuthorizationHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteLocatorConfig {
    //라우터 설정, 모든 요청이 gateway를 지나게 한다.
    private final JwtAuthorizationHeaderFilter jwtAuthorizationHeaderFilter;


    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                //인증
                //TODO account api 경로 p.path에 설정 (아직 account 경로 정하지 않음)
                .route("authentication", p -> p.path("/api/login")
//                        .filters(f->f.filter(jwtAuthorizationHeaderFilter.apply(new JwtAuthorizationHeaderFilter.Config())))
                        .uri("lb://SHOPPINGMALL-SERVICE/api/login")
                )
                //쇼핑몰
                //TODO account api 경로 p.path에 설정 (아직 shoppingmall 경로 정하지 않음)
                .route("bookstore-shoppingmall", p -> p.path("/api/shop/***")
                                .and()
//                              .weight("bookstore-shoppingmall", 50)  쿠폰 서버가 분리 될 경우 추가
                                .uri("lb://localhost:8081")
                ).build();

    }
}

