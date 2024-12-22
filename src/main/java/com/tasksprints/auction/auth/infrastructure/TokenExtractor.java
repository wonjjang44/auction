package com.tasksprints.auction.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    String extractToken(HttpServletRequest request);
}
