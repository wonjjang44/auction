package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import com.tasksprints.auction.domain.auth.exception.AuthException;
import com.tasksprints.auction.domain.auth.model.RefreshToken;
import com.tasksprints.auction.domain.auth.repository.RefreshTokenRepository;
import com.tasksprints.auction.domain.user.dto.response.UserDetailResponse;
import com.tasksprints.auction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public UserTokens login(String email, String password) {
        UserDetailResponse userDetail = userService.getUserDetailByEmail(email);

        if (!password.equals(userDetail.getPassword())) {
            throw new AuthException("password is not correct");
        }

        UserTokens userTokens = jwtProvider.generateToken(userDetail.getId().toString());
        RefreshToken refreshToken = RefreshToken.create(userTokens.getRefreshToken(), userDetail.getId());
        refreshTokenRepository.save(refreshToken);
        return userTokens;
    }
}
