package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.domain.auth.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken saveRefreshToken(String refreshTokenValue, Long userId);
}
