package com.tasksprints.auction.auth.application;

import com.tasksprints.auction.auth.exception.RefreshTokenException;
import com.tasksprints.auction.auth.domain.model.RefreshToken;
import com.tasksprints.auction.auth.infrastructure.RefreshTokenRepository;
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

    @Override
    public RefreshToken findRefreshTokenById(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken)
            .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));
    }
}
