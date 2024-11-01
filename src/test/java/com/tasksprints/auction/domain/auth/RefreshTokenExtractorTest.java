package com.tasksprints.auction.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tasksprints.auction.domain.auth.exception.RefreshTokenException;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RefreshTokenExtractorTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenExtractor tokenExtractor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("쿠키에서 refresh token 을 꺼내서 반환한다.")
    void testExtractToken_success() {
        // given
        Cookie[] cookies = {
            new Cookie("nothing", "token"),
            new Cookie("refresh-token", "tokenName"),
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        when(refreshTokenRepository.existsById("tokenName")).thenReturn(true);

        // when
        String resultValue = tokenExtractor.extractToken(request);

        // then
        assertThat(resultValue).isEqualTo("tokenName");
    }

    @Test
    @DisplayName("쿠키에 refresh token이 존재하지 않으면 예외를 반환한다.")
    void testExtractToken_fail() {
        // given
        Cookie[] cookies = {
            new Cookie("nothing", "token"),
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);

        // when, then
        Assertions.assertThrows(RefreshTokenException.class, ()-> {
            tokenExtractor.extractToken(request);
        }, "리프레시토큰이 쿠키에 존재해야 합니다.");
    }
}
