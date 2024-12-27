package com.nhnacademy.bookstoregateway.filter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtAuthorizationHeaderFilter extends AbstractGatewayFilterFactory<JwtAuthorizationHeaderFilter.Config> {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public JwtAuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if ("/api/members".equals(request.getURI().getPath())) {
                //여기에 uri 추가하거나 static으로 인증이 필요 없는 uri 정의 해둔다
                log.debug("인증 필요 없이 통과");
                return chain.filter(exchange);
            }


            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.error("Authorization 헤더가 없다!");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 없다!");
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.error("authHeader의 형식이 잘못 됨!");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"authHeader의 형식이 잘못 됐다!");
            }

            String token = authHeader.substring(7); // "Bearer " 이후의 토큰

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                log.debug("JWT 검증 성공, 사용자 이메일 : {}", email);


                // 클라이언트 요청에 'X-USER-ID' 헤더 추가
                exchange = exchange.mutate().request(builder -> builder.header("X-USER-ID", claims.getSubject())).build();

            } catch (ExpiredJwtException e) {
                // 토큰이 만료된 경우 예외 처리
                log.error("JWT 토큰이 만료되었습니다.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다.");
            }  catch (MalformedJwtException e) {
                // JWT의 형식이 잘못된 경우 처리
                log.error("잘못된 JWT 형식입니다.", e);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 JWT 형식입니다.");

            } catch (SignatureException e) {
                // JWT 서명이 유효하지 않은 경우 처리
                log.error("JWT 서명이 유효하지 않습니다.", e);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 서명이 유효하지 않습니다.");

            } catch (IllegalArgumentException e) {
                // JWT에 필요한 정보가 누락된 경우 처리
                log.error("JWT가 잘못되었습니다.", e);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT가 잘못되었습니다.");

            } catch (Exception e) {
                // 그 외 예외 처리
                log.error("JWT 검증 실패", e);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT");
            }

            return chain.filter(exchange);
        };
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}