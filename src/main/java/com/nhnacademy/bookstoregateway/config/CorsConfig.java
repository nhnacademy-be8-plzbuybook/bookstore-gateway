package com.nhnacademy.bookstoregateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000"); // 프론트엔드 도메인
        corsConfig.addAllowedOrigin("http://localhost:8090"); // 다른 도메인들 추가

        corsConfig.addAllowedMethod("*");  // 모든 메소드 허용
        corsConfig.addAllowedHeader("*");  // 모든 헤더 허용
        corsConfig.setAllowCredentials(true);  // 자격 증명 허용
        corsConfig.setMaxAge(3600L);  // 1시간 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);  // 모든 요청에 대해 CORS 적용

        return new CorsWebFilter(source);
    }
}