package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.domain.auth.model.RefreshToken;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {
    RefreshToken saveRefreshToken(String refreshTokenValue, Long userId);

    ResponseCookie getResponseRefreshToken(String refresh);

    RefreshToken findRefreshTokenById(String refreshToken);
}
