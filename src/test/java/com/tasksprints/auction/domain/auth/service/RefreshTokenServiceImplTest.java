package com.tasksprints.auction.domain.auth.service;

import static org.mockito.Mockito.*;

import com.tasksprints.auction.domain.auth.model.RefreshToken;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    RefreshTokenServiceImpl refreshTokenService;

    @Nested
    @DisplayName("Save RefreshToken test")
    class saveRefreshTokenTest {
        @Test
        @DisplayName("should return RefreshToken, when the test is success")
        void testSaveRefreshToken_success() {
            // given
            String refreshTokenValue = "refreshToken";
            Long userId = 1L;
            RefreshToken expectedRefreshToken = RefreshToken.of(refreshTokenValue, userId);
            when(refreshTokenRepository.save(any())).thenReturn(expectedRefreshToken);

            // when
            RefreshToken actualRefreshToken = refreshTokenService.saveRefreshToken(refreshTokenValue, userId);

            // then
            Assertions.assertEquals(expectedRefreshToken.getMemberId(), actualRefreshToken.getMemberId());
            Assertions.assertEquals(expectedRefreshToken.getId(), actualRefreshToken.getId());
        }
    }
}
