package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.domain.auth.dto.response.ResponseTokens;

public interface AuthService {
    Long validateLogin(String email, String password);

    ResponseTokens issueResponseTokens(Long userId);
}
