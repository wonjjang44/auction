package com.tasksprints.auction.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tasksprints.auction.domain.auth.exception.RefreshTokenException;
import com.tasksprints.auction.domain.auth.model.RefreshToken;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import java.util.Optional;
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
    class TestSaveRefreshToken {
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
            assertEquals(expectedRefreshToken.getUserId(), actualRefreshToken.getUserId());
            assertEquals(expectedRefreshToken.getId(), actualRefreshToken.getId());
        }
    }

    @Nested
    @DisplayName("Find refresh Token test")
    class TestFindRefreshToken {

        void testFindRefreshToken_success() {
            // given
            String refreshTokenValue = "refreshToken";
            RefreshToken existedRefreshToken = RefreshToken.of("refreshToken", 1L);
            when(refreshTokenRepository.findById(any())).thenReturn(Optional.ofNullable(existedRefreshToken));

            // when
            RefreshToken foundRefreshToken = refreshTokenService.findRefreshTokenById(refreshTokenValue);

            // then
            assertEquals(1L, foundRefreshToken.getUserId());
            assertEquals(refreshTokenValue, foundRefreshToken.getId());
        }

        void testFindRefreshToken_fail() {
            // given
            String refreshTokenValue = "refreshToken";
            when(refreshTokenRepository.findById(any())).thenReturn(Optional.empty());

            // when, then
            Assertions.assertThrows(RefreshTokenException.class, () -> {
                refreshTokenService.findRefreshTokenById(refreshTokenValue);
            });
        }
    }
}
