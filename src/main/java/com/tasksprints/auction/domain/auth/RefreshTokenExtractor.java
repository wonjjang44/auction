package com.tasksprints.auction.domain.auth;

import static com.tasksprints.auction.common.constant.ApiResponseMessages.REFRESH_TOKEN_NOT_FOUND;

import com.tasksprints.auction.domain.auth.exception.RefreshTokenException;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("refreshTokenExtractor")
@RequiredArgsConstructor
public class RefreshTokenExtractor implements TokenExtractor {
    private static final String COOKIE_NAME = "refresh-token";

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public String extractToken(HttpServletRequest request) {
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
}
