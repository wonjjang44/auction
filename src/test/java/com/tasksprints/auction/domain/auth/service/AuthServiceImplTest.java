package com.tasksprints.auction.domain.auth.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import com.tasksprints.auction.domain.user.dto.response.UserDetailResponse;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserService userService;

    @Mock
    JwtProvider jwtProvider;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private AuthServiceImpl authService;

    private UserDetailResponse userDetail;

    @BeforeEach
    void setUp() {
        User existingUser = User.builder()
            .id(1L)
            .email("user@exapmle.com")
            .password("password")
            .nickName("testUser")
            .name("realName")
            .build();

        userDetail = UserDetailResponse.of(existingUser);
    }

    @Nested
    @DisplayName("Login success when password is correct")
    class testLogin {
        @Test
        @DisplayName("Return tokens when password is correct")
        void ReturnTokensWhenPasswordIsCorrect() {
            // given
            String email = "user@exapmle.com";
            String password = "password";
            UserTokens expected = UserTokens.of("accessToken", "refreshToken");
            when(userService.getUserDetailByEmail(any())).thenReturn(userDetail);
            when(jwtProvider.generateToken(any())).thenReturn(expected);
            when(refreshTokenRepository.save(any())).thenReturn(any());

            // when
            UserTokens actual = authService.login(email, password);

            // then
            assertEquals(expected.getRefreshToken(), actual.getRefreshToken());
            assertEquals(expected.getAccessToken(), actual.getAccessToken());
        }

        @Test
        @DisplayName("should throw exception when password is different")
        void shouldReturnExceptionWhenPasswordIsDifferent() {
            // given
            String email = "user@exapmle.com";
            String password = "differentPassword";
            when(userService.getUserDetailByEmail(any())).thenReturn(userDetail);

            // when
            AuthException exception = assertThrows(AuthException.class, () -> {
                authService.login(email, password);
            });

            // then
            assertEquals("password is not correct", exception.getMessage());
        }
    }
}
