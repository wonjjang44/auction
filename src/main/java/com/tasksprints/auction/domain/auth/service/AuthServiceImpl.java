package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.common.jwt.JwtProvider;
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

    private static final Integer COOKIE_AGE_SECONDS = 1209600;

    @Transactional
    @Override
    public UserTokens login(String email, String password) {
        UserDetailResponse userDetail = userService.getUserDetailByEmail(email);

        if (!password.equals(userDetail.getPassword())) {
            throw new AuthException("password is not correct");
        }

        UserTokens userTokens = jwtProvider.generateToken(userDetail.getId().toString());
        refreshTokenService.saveRefreshToken(userTokens.getRefreshToken(), userDetail.getId());

        return userTokens;
    }

    @Override
    public ResponseCookie getResponseCookie(String refreshToken) {
        return ResponseCookie.from("refresh-token", refreshToken)
            .maxAge(COOKIE_AGE_SECONDS)
            .secure(true)
            .httpOnly(true)
            .sameSite("None")
            .path("/")
            .build();
    }
}
