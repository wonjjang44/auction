package com.tasksprints.auction.domain.auth.dto.response;

public record AccessToken(String accessToken) {
    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
