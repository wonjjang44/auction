package com.tasksprints.auction.domain.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    String extractToken(HttpServletRequest request);
}
