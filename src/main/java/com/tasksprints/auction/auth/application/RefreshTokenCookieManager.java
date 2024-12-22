package com.tasksprints.auction.auth.application;

import static com.tasksprints.auction.common.constant.ApiResponseMessages.REFRESH_TOKEN_NOT_FOUND;

import com.tasksprints.auction.auth.exception.RefreshTokenException;
import com.tasksprints.auction.auth.infrastructure.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieManager {
    private static final Integer COOKIE_AGE_SECONDS = 1209600;
    private static final String COOKIE_NAME = "refresh-token";

    private final RefreshTokenRepository refreshTokenRepository;

    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new RefreshTokenException(REFRESH_TOKEN_NOT_FOUND);
        }
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
            .filter(cookie -> refreshTokenRepository.existsById(cookie.getValue()))
            .findFirst()
            .orElseThrow(() -> new RefreshTokenException(REFRESH_TOKEN_NOT_FOUND))
            .getValue();
    }

    public ResponseCookie createResponseCookie(String refreshToken) {
        return ResponseCookie.from(COOKIE_NAME, refreshToken)
            .maxAge(COOKIE_AGE_SECONDS)
            .secure(true)
            .httpOnly(true)
            .sameSite("None")
            .path("/")
            .build();
    }
}
