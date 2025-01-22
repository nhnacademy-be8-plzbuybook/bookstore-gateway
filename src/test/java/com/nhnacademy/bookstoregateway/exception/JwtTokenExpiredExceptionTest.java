package com.nhnacademy.bookstoregateway.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenExpiredExceptionTest {

    @Test
    void testJwtTokenExpiredExceptionMessage() {
        String expectedMessage = "JWT 토큰이 만료되었습니다.";
        JwtTokenExpiredException exception = new JwtTokenExpiredException(expectedMessage);

        // 예외의 메시지가 기대한 값과 일치하는지 확인
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testJwtTokenExpiredExceptionConstructor() {
        String expectedMessage = "JWT 토큰이 만료되었습니다.";
        JwtTokenExpiredException exception = new JwtTokenExpiredException(expectedMessage);

        // 예외가 제대로 생성되었는지 확인
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
}