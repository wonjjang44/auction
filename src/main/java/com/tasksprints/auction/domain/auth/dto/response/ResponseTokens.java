package com.tasksprints.auction.domain.auth.dto.response;

import lombok.Builder;
import org.springframework.http.ResponseCookie;

@Builder
public record ResponseTokens(AccessToken accessToken, ResponseCookie refreshToken) {
    public static ResponseTokens of(AccessToken accessToken, ResponseCookie refreshToken) {
        return ResponseTokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
