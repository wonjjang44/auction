package com.tasksprints.auction.api.auth;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.api.BaseControllerTest;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.domain.auth.dto.request.LoginRequest;
import com.tasksprints.auction.domain.auth.dto.request.LoginRequest.Login;
import com.tasksprints.auction.domain.auth.dto.response.AccessToken;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.auth.service.AuthService;
import com.tasksprints.auction.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    @DisplayName("test login")
    class LoginTest {

        private final ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "refreshTokenValue")
            .maxAge(3600)
            .secure(true)
            .httpOnly(true)
            .sameSite("None")
            .path("/")
            .build();


        @Test
        @DisplayName("Return refresh and access token, when login success")
        void login_success() throws Exception {
            // given
            AccessToken accessToken = AccessToken.of("accessTokenValue");
            UserTokens tokens = UserTokens.of(accessToken, "refreshTokenValue");
            LoginRequest.Login request = new Login("example@email.com", "password");
            when(authService.login(any(), any())).thenReturn(tokens);
            when(authService.getResponseCookie(any())).thenReturn(responseCookie);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("accessTokenValue"))
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.LOGIN_SUCCESS))
                .andExpect(header().string("set-cookie", containsString("refresh-token=refreshTokenValue")))
                .andExpect(header().string("set-cookie", containsString("Max-Age=3600")))
                .andExpect(header().string("set-cookie", containsString("Secure")))
                .andExpect(header().string("set-cookie", containsString("HttpOnly")))
                .andExpect(header().string("set-cookie", containsString("SameSite=None")));
        }

        @Test
        @DisplayName("Throw Exception, when password is different")
        void loginFailWhenPasswordIsDifferent() throws Exception {
            // given
            LoginRequest.Login request = new Login("example@email.com", "password");
            when(authService.login(any(), any())).thenThrow(AuthException.class);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.USER_NOT_FOUND));
        }

        @Test
        @DisplayName("Throw Exception, when email is different")
        void loginFailWhenEmailIsDifferent() throws Exception {
            // given
            LoginRequest.Login request = new Login("example@email.com", "password");
            when(authService.login(any(), any())).thenThrow(UserNotFoundException.class);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.USER_NOT_FOUND));
        }
    }
}

