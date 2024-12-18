package com.tasksprints.auction.common.jwt.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String refreshToken;
    private String accessToken;

    public static JwtResponse of(String accessToken, String refreshToken) {
        return JwtResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
