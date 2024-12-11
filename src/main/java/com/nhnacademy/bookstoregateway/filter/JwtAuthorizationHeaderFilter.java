
package com.nhnacademy.bookstoregateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtAuthorizationHeaderFilter extends AbstractGatewayFilterFactory<JwtAuthorizationHeaderFilter.Config> {

    //jwt검증에 사용할 비밀 키 (아무렇게나 지어도 무방)
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;



    public JwtAuthorizationHeaderFilter(){
        super(Config.class);
    }

    public static class Config {
        //application.properties 파일에서 지정한 filter의 Argument값을 받는 부분
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    //필터 동작 정의
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //jwt 검증 필터
            log.debug("jwt-validation-filter");
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.error("Authorization 헤더가 없습니다!");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 없다!");
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            //헤더 형식이 잘못 된 경우 예외처리
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.error("authHeader의 형식이 잘못 됨!");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"authHeader의 형식이 잘못 됐다!");
            }

            String token = authHeader.substring(7); //Bearer 이후

            try {
                //jwt토큰 파싱하여 claim객체를 가져옴
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())  //비밀키를 통한 토큰의 서명 검증
                        .build()
                        .parseClaimsJws(token).
                        getBody();

                String userId = claims.getSubject();
                log.debug("JWT 검증 성공, 사용자 아이디 : {}", userId);

                //요청 헤더에 x-user-id를 추가하여 사용자 id정보를 포함시킴
                //즉 헤더에 x-user-id가 있다면 jwt가 검증 된 것임을 나타냄
                exchange = exchange.mutate().request((builder -> builder.header("X-USER-ID", userId))).build();

                }catch (SignatureException e){
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 서명 검증 실패!");
                }catch (Exception e){
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 jwt!");
                }

            return chain.filter(exchange);
        };
    }
}
//프론트에서 로그인 -> 인증서버가 자격 증명을 확인하고 인증 성공하면 jwt 프론트에 반환 -> 프론트엔드는 반환받능 jwt를 (로컬 스토리지 or 세션 스토리지 or 쿠키에 저장)
//-> jwt를 authoriztion헤더에 포함시켜 서버로 전송 Authorization : Bearer < JWT >
//게이트 웨이에서 Authorization의 헤더를 확인하고 jwt 검증 필터 실행(검증 필터가 JwtAuthorizationHeaderFilter의 역할)
//검증이 완료 되면 헤더에 x-user-id , <userId>를 추가함 ==> 검증된 사용자라는 뜻
//백엔드는 요청을 인증된 사용자임을 확인하고 적절히 처리

