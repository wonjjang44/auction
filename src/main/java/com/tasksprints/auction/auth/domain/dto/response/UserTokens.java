package com.tasksprints.auction.auth.domain.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class UserTokens {
    private String refreshToken;
    private AccessToken accessToken;

    public static UserTokens of(AccessToken accessToken, String refreshToken) {
        return UserTokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
