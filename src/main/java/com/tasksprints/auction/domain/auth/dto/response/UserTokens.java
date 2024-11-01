package com.tasksprints.auction.domain.auth.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class UserTokens {
    private String refreshToken;
    private String accessToken;

    public static UserTokens of(String accessToken, String refreshToken) {
        return UserTokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
