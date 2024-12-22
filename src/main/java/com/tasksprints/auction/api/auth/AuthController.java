package com.tasksprints.auction.api.auth;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.auth.dto.request.LoginRequest;
import com.tasksprints.auction.domain.auth.dto.response.AccessToken;
import com.tasksprints.auction.domain.auth.dto.response.ResponseTokens;
import com.tasksprints.auction.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<AccessToken>> login(@RequestBody LoginRequest.Login login) {
        Long userId = authService.validateLogin(login.email(), login.password());
        ResponseTokens responseTokens = authService.issueResponseTokens(userId);

        return ResponseEntity.ok()
            .header(SET_COOKIE, responseTokens.refreshToken().toString())
            .body(ApiResult.success(ApiResponseMessages.LOGIN_SUCCESS, responseTokens.accessToken()));
    }

    @GetMapping("/reissue")
    public ResponseEntity<ApiResult<AccessToken>> reissueTokens(@CookieValue("refresh-token") String refreshToken) {
        ResponseTokens responseTokens = authService.reissueResponseTokens(refreshToken);

        return ResponseEntity.ok()
            .header(SET_COOKIE, responseTokens.refreshToken().toString())
            .body(ApiResult.success(ApiResponseMessages.LOGIN_SUCCESS, responseTokens.accessToken()));
    }
}
