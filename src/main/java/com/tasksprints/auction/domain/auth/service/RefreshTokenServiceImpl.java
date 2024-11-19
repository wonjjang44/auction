package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.domain.auth.model.RefreshToken;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCookieManager cookieManager;

    @Override
    @Transactional
    public RefreshToken saveRefreshToken(String refreshTokenValue, Long userId) {
        RefreshToken refreshToken = RefreshToken.of(refreshTokenValue, userId);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public ResponseCookie getResponseRefreshToken(String refreshToken) {
        return cookieManager.createResponseCookie(refreshToken);
    }
}
