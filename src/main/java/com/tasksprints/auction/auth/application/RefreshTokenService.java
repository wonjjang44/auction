package com.tasksprints.auction.auth.application;

import com.tasksprints.auction.auth.domain.model.RefreshToken;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {
    RefreshToken saveRefreshToken(String refreshTokenValue, Long userId);

    ResponseCookie getResponseRefreshToken(String refresh);

    RefreshToken findRefreshTokenById(String refreshToken);
}
