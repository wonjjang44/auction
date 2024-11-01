package com.tasksprints.auction.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.tasksprints.auction.domain.auth.exception.AccessTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccessTokenExtractorTest {
    private TokenExtractor tokenExtractor;

    @BeforeEach
    void setUp() {
        tokenExtractor = new AccessTokenExtractor();
    }

    @Test
    @DisplayName("헤더에서 access token 을 꺼낸다")
    void testExtractToken_success() {
        // given
        String header = "Bearer token";

        // when
        String accessToken = tokenExtractor.extractToken(header);

        // then
        assertThat(accessToken).isEqualTo("token");
    }

    @Test
    @DisplayName("access token 이 존재하지 않으면 예외를 반환한다")
    void testExtractToken_fail() {
        // given
        String header = "no token";

        // when, then
        Assertions.assertThrows(AccessTokenException.class, () -> {
            tokenExtractor.extractToken(header);
        });
    }
}
