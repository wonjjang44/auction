package com.tasksprints.auction.auth.application;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.auth.domain.dto.response.ResponseTokens;
import com.tasksprints.auction.auth.domain.dto.response.UserTokens;
import com.tasksprints.auction.auth.exception.AuthException;
import com.tasksprints.auction.user.application.service.UserService;
import com.tasksprints.auction.user.domain.dto.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Long validateLogin(String email, String password) {
        UserDetailResponse userDetail = userService.getUserDetailByEmail(email);
        log.info(userDetail.toString());
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
