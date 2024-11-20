package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.dto.response.ResponseTokens;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.user.dto.response.UserDetailResponse;
import com.tasksprints.auction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Long validateLogin(String email, String password) {
        UserDetailResponse userDetail = userService.getUserDetailByEmail(email);

        if (!password.equals(userDetail.getPassword())) {
            throw new AuthException("password is not correct");
        }
        return userDetail.getId();
    }

    @Transactional
    @Override
    public ResponseTokens issueResponseTokens(Long userId) {
        UserTokens tokens = jwtProvider.generateToken(userId.toString());
        refreshTokenService.saveRefreshToken(tokens.getRefreshToken(), userId);
        ResponseCookie refreshToken = refreshTokenService.getResponseRefreshToken(tokens.getRefreshToken());

        return ResponseTokens.of(tokens.getAccessToken(), refreshToken);
    }

    @Transactional
    @Override
    public ResponseTokens reissueResponseTokens(String refreshToken) {
        Long userId = refreshTokenService.findRefreshTokenById(refreshToken).getUserId();
        return issueResponseTokens(userId);
    }
}
