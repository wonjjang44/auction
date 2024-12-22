package com.tasksprints.auction.domain.auth.dto.request;

import lombok.Builder;


public class LoginRequest {
    @Builder
    public record Login(String email, String password) {
        public static Login of(String email, String password) {
            return Login.builder()
                .email(email)
                .password(password)
                .build();
        }
    }
}
