package com.tasksprints.auction.domain.auth.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.dto.response.AccessToken;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.user.dto.response.UserDetailResponse;
import com.tasksprints.auction.domain.user.exception.UserNotFoundException;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.service.UserService;
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

    private String loginEmail;

    @BeforeEach
    void setUp() {
        loginEmail = "user@exapmle.com";
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
    @DisplayName("Validate login information test")
    class TestLogin {
        @Test
        @DisplayName("Return userDetails, when password same")
        void validateLogin_success() {
            // given
            String password = "password";
            when(userService.getUserDetailByEmail(any())).thenReturn(userDetail);

            // when
            Long actualUserId = authService.validateLogin(loginEmail, password);

            // then
            assertEquals(1L, actualUserId);
        }

        @Test
        @DisplayName("Should throw exception when password is different")
        void validateLoginDifferentPassword() {
            // given
            String password = "differentPassword";
            when(userService.getUserDetailByEmail(any())).thenReturn(userDetail);

            // when
            AuthException exception = assertThrows(AuthException.class, () -> {
                authService.validateLogin(loginEmail, password);
            });

            // then
            assertEquals("password is not correct", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Issue tokens test")
    class TestIssueTokens {

        @Test
        @DisplayName("Return tokens, when issue tokens successfully")
        void returnTokens_success() {
            // given
            Long userId = 1L;
            AccessToken accessToken = AccessToken.of("accessTokenValue");
            String refreshToken = "refreshTokenValue";
            UserTokens generated = UserTokens.of(accessToken, refreshToken);
            when(jwtProvider.generateToken(any())).thenReturn(generated);
            when(refreshTokenService.saveRefreshToken(any(), any())).thenReturn(any());

            // when
            UserTokens issued = authService.issueTokens(userId);

            // then
            assertEquals(generated.getAccessToken().accessToken(), issued.getAccessToken().accessToken());
            assertEquals(refreshToken, issued.getRefreshToken());
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
            assertEquals(1209600, responseCookie.getMaxAge().toSeconds());
            assertTrue(responseCookie.isSecure());
            assertTrue(responseCookie.isHttpOnly());
            assertEquals("None", responseCookie.getSameSite());
            assertEquals("/", responseCookie.getPath());
        }
    }
}
