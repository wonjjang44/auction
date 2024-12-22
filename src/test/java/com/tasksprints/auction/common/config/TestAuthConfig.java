package com.tasksprints.auction.common.config;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.TokenExtractor;
import com.tasksprints.auction.domain.auth.service.RefreshTokenCookieManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestAuthConfig {
    @MockBean
    public JwtProvider jwtProvider;

    @MockBean
    public RefreshTokenCookieManager refreshTokenCookieManager;

    @MockBean
    public TokenExtractor accessTokenExtractor;
}
