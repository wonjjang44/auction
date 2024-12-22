package com.tasksprints.auction.auth.domain.dto.response;

public record AccessToken(String accessToken) {
    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
