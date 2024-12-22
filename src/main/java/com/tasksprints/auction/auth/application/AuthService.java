package com.tasksprints.auction.auth.application;

import com.tasksprints.auction.auth.domain.dto.response.ResponseTokens;

public interface AuthService {
    Long validateLogin(String email, String password);

    ResponseTokens issueResponseTokens(Long userId);

    ResponseTokens reissueResponseTokens(String refreshToken);
}
