package com.tasksprints.auction.common.config;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.TokenExtractor;
import com.tasksprints.auction.domain.auth.service.RefreshTokenCookieManager;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestAuthConfig {
    @Bean
    public JwtProvider jwtProvider() {
        return Mockito.mock(JwtProvider.class);
    }

    @Bean
    public RefreshTokenCookieManager refreshTokenCookieManager() {
        return Mockito.mock(RefreshTokenCookieManager.class);
    }

    @Bean
    public TokenExtractor accessTokenExtractor() {
        return Mockito.mock(TokenExtractor.class);
    }
}
