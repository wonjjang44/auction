package com.tasksprints.auction.domain.auth.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.dto.response.AccessToken;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.user.dto.response.UserDetailResponse;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.service.UserService;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserService userService;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    RefreshTokenService refreshTokenService;
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
    @DisplayName("Login success test")
    class TestLogin {
        @Test
        @DisplayName("Return tokens when password is correct")
        void returnTokensWhenPasswordIsCorrect() {
            // given
            String email = "user@exapmle.com";
            String password = "password";
            AccessToken accessToken = AccessToken.of("accessToken");
            UserTokens expected = UserTokens.of(accessToken, "refreshToken");
            when(userService.getUserDetailByEmail(any())).thenReturn(userDetail);
            when(jwtProvider.generateToken(any())).thenReturn(expected);
            when(refreshTokenService.saveRefreshToken(any(), any())).thenReturn(any());

            // when
            UserTokens actual = authService.login(email, password);

            // then
            assertEquals(expected.getRefreshToken(), actual.getRefreshToken());
            assertEquals(expected.getAccessToken(), actual.getAccessToken());
        }

        @Test
        @DisplayName("Should throw exception when password is different")
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

    @Nested
    @DisplayName("Get response cookie test")
    class TestResponseCookie {

        @Test
        @DisplayName("Return ResponseCookie, when creating the cookie successfully")
        void returnResponseCookie_success() {
            // given
            String refreshToken = "refreshTokenValue";

            // when
            ResponseCookie responseCookie = authService.getResponseCookie(refreshToken);

            // then
            Assertions.assertEquals(1209600, responseCookie.getMaxAge().toSeconds());
            Assertions.assertTrue(responseCookie.isSecure());
            Assertions.assertTrue(responseCookie.isHttpOnly());
            Assertions.assertEquals("None", responseCookie.getSameSite());
            Assertions.assertEquals("/", responseCookie.getPath());
        }
    }
}
