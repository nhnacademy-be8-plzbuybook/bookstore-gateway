package com.nhnacademy.bookstoregateway.config;

import com.nhnacademy.bookstoregateway.filter.CustomGlobalFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FilterConfig {

    //global filter를 bean으로 등록
    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }
}
