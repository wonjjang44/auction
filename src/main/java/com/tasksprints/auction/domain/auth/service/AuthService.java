package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    Long validateLogin(String email, String password);

    ResponseCookie getResponseCookie(String refreshToken);

    UserTokens issueTokens(Long userId);
}
