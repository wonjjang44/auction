package com.tasksprints.auction.domain.auth;

public interface TokenExtractor {
    String extractToken(String value);
}
