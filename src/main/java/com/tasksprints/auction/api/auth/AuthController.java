package com.tasksprints.auction.api.auth;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.auth.dto.request.LoginRequest;
import com.tasksprints.auction.domain.auth.dto.response.AccessToken;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private static final Integer COOKIE_AGE_SECONDS = 1209600;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<AccessToken>> login(@RequestBody LoginRequest.Login login) {
        UserTokens tokens = authService.login(login.email(), login.password());
        AccessToken accessToken = AccessToken.of(tokens.getAccessToken());

        ResponseCookie cookie = ResponseCookie.from("refresh-token", tokens.getRefreshToken())
            .maxAge(COOKIE_AGE_SECONDS)
            .secure(true)
            .httpOnly(true)
            .sameSite("None")
            .path("/")
            .build();

        return ResponseEntity.ok()
            .header(SET_COOKIE, cookie.toString())
            .body(ApiResult.success(ApiResponseMessages.LOGIN_SUCCESS, accessToken));
    }
}
