package com.tasksprints.auction.domain.auth.service;

import com.tasksprints.auction.domain.auth.dto.response.UserTokens;

public interface AuthService {
    UserTokens login(String email, String password);
}
