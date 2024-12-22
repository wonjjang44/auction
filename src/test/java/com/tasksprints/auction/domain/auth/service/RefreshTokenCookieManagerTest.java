package com.tasksprints.auction.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tasksprints.auction.domain.auth.exception.RefreshTokenException;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseCookie;

class RefreshTokenCookieManagerTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenCookieManager cookieManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("")
    class TestExtractToken {
        @Test
        @DisplayName("Return refresh token, after extracting refresh token")
        void extractRefreshToken_success() {
            // given
            Cookie[] cookies = {
                new Cookie("nothing", "token"),
                new Cookie("refresh-token", "tokenName"),
            };
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(cookies);
            when(refreshTokenRepository.existsById("tokenName")).thenReturn(true);

            // when
            String resultValue = cookieManager.extractRefreshToken(request);

            // then
            assertThat(resultValue).isEqualTo("tokenName");
        }

        @Test
        @DisplayName("Should throw exception, when refresh token doesn't exist in cookie")
        void extractRefreshToken_fail() {
            // given
            Cookie[] cookies = {
                new Cookie("nothing", "token"),
            };
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(cookies);

            // when, then
            Assertions.assertThrows(RefreshTokenException.class, () -> {
                cookieManager.extractRefreshToken(request);
            }, "리프레시토큰이 쿠키에 존재해야 합니다.");
        }

    }


    @Nested
    @DisplayName("Get response cookie test")
    class TestResponseCookie {

        @Test
        @DisplayName("Return ResponseCookie, when creating the cookie successfully")
        void returnResponseCookie_success() {
            // given
            String refreshToken = "refreshTokenValue";

            // when
            ResponseCookie responseCookie = cookieManager.createResponseCookie(refreshToken);

            // then
            assertEquals(1209600, responseCookie.getMaxAge().toSeconds());
            assertTrue(responseCookie.isSecure());
            assertTrue(responseCookie.isHttpOnly());
            assertEquals("None", responseCookie.getSameSite());
            assertEquals("/", responseCookie.getPath());
        }
    }
}
