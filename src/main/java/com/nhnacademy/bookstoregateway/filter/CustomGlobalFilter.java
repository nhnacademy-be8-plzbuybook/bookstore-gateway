package com.nhnacademy.bookstoregateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    //global filter url 상관없이 전역적으로 동작

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("global filter");
        return chain.filter(exchange);
    }

    @Override
    //Ordered 인터페이스 구현 후 filter의 우선순위 설정 가능
    public int getOrder() {
        return -1;
    }
}
