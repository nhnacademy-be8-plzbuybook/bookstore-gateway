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
                .route("books",  p -> p.path("/api/books/**")
                        .uri("lb://BOOKSTORE")
                )
                .route("auth", p -> p.path("/api/auth/**")
                        .uri("lb://AUTHENTICATION")
                )
                .route("shoppingmall", p -> p.path("/api/members/**")
                        .filters(f -> f.filter(jwtAuthorizationHeaderFilter.apply(new JwtAuthorizationHeaderFilter.Config())))
                        .uri("lb://BOOKSTORE")
                )
                // 권한 부여 필요.
                .route("bookstore", p->p.path("/api/objects/**")
                        .uri("lb://BOOKSTORE")
                )
                .route("coupon", p->p.path("/api/coupons/**")
                        .uri("lb://COUPON")
                )
                .build();
    }
}
