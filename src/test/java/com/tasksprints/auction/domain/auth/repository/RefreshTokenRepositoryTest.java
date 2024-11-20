package com.tasksprints.auction.domain.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.tasksprints.auction.common.config.QueryDslConfig;
import com.tasksprints.auction.domain.auth.model.RefreshToken;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import(QueryDslConfig.class)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = RefreshToken.builder()
            .id("testId")
            .userId(1L)
            .build();
    }

    @Test
    @DisplayName("refresh token ID로  refresh token 조회")
    void testFindById() {
        // given
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> resultRefreshToken = refreshTokenRepository.findById(refreshToken.getId());

        // then
        assertTrue(resultRefreshToken.isPresent());
        assertThat(refreshToken.getId()).isEqualTo(resultRefreshToken.get().getId());
    }

    @Test
    @DisplayName("refresh token ID로  refresh token이 존재하는지 확인합니다.")
    void testExistById() {
        // given
        refreshTokenRepository.save(refreshToken);

        // when
        boolean resultRefreshToken = refreshTokenRepository.existsById(refreshToken.getId());

        // then
        assertTrue(resultRefreshToken);
    }
}
