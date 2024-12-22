package com.tasksprints.auction.common.jwt;

import com.tasksprints.auction.common.config.JwtConfig;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    @Mock
    private JwtConfig jwtConfig;
    @Mock
    private Clock clock;
    @InjectMocks
    private JwtProvider jwtProvider;

    private final Long VALID_EXPIRE_MS = 36000000L;
    private final Long REFRESH_EXPIRE_MS = 72000000L;
    private final Long EXPIRED_EXPIRE_MS = 0L;
    private final String ISSUER = "testIssuer";
    private final String SECRET_KEY = "testSecretKey";
    private final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    @BeforeEach
    public void setUp() {
        when(clock.instant()).thenReturn(Instant.now());
        when(clock.getZone()).thenReturn(ZONE_ID);
        when(jwtConfig.getIssuer()).thenReturn(ISSUER);
        when(jwtConfig.getSecretKey()).thenReturn(SECRET_KEY);
    }

    private void stubAccessTokenExpiration(Long expireMs) {
        when(jwtConfig.getAccessExpireMs()).thenReturn(expireMs);
    }

    private void stubRefreshTokenExpiration(Long expireMs) {
        when(jwtConfig.getRefreshExpireMs()).thenReturn(expireMs);
    }

    @Test
    @DisplayName("accessToken과 refreshToken을 발급해야한다.")
    void generateToken() {
        // when
        UserTokens userTokens = jwtProvider.generateToken("1L");

        // then
        assertNotNull(userTokens.getAccessToken(), "access token 이 발급되어야 합니다.");
        assertNotNull(userTokens.getRefreshToken(), "refresh token 이 발급되어야 합니다.");
    }

    @Test
    @DisplayName("유효기간에는 토큰이 유효해야한다.")
    void verifyToken_valid() {
        // given
        stubAccessTokenExpiration(VALID_EXPIRE_MS);
        stubRefreshTokenExpiration(REFRESH_EXPIRE_MS);

        // when
        UserTokens userTokens = jwtProvider.generateToken("1L");

        // then
        Assertions.assertDoesNotThrow(() -> {
            jwtProvider.validateToken(userTokens.getAccessToken().accessToken());
        });
        Assertions.assertDoesNotThrow(() -> {
            jwtProvider.validateToken(userTokens.getRefreshToken());
        });
    }

    @Test
    @DisplayName("유효기간이 지나면, 토큰 만료 예외를 반환해야한다")
    void verifyToken_expired() {
        // given
        stubAccessTokenExpiration(EXPIRED_EXPIRE_MS);
        stubRefreshTokenExpiration(EXPIRED_EXPIRE_MS);

        // when
        UserTokens userTokens = jwtProvider.generateToken("1L");

        // then
        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtProvider.validateToken(userTokens.getRefreshToken());
        }, "리프레시토큰이 즉시 만료되어야 합니다.");

        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtProvider.validateToken(userTokens.getAccessToken().accessToken());
        }, "액세스토큰이 즉시 만료되어야 합니다.");
    }

    @Test
    @DisplayName("디코딩 된 페이로드 정확성 테스트")
    void getClaims() {
        // given
        stubAccessTokenExpiration(VALID_EXPIRE_MS);
        UserTokens userTokens = jwtProvider.generateToken("1L");

        // when
        String decodedUserId = jwtProvider.getSubject(userTokens.getAccessToken().accessToken());

        // then
        assertThat(decodedUserId).isEqualTo("1L");
    }
}
