package com.tasksprints.auction.auth.domain.model;

import lombok.Builder;

@Builder
public record Accessor(Long userId, Role role) {

    private static final Long GUEST_USER_ID = 0L;

    public static Accessor guest() {
        return Accessor.of(GUEST_USER_ID, Role.GUEST);
    }

    public static Accessor user(Long userId) {
        return Accessor.of(userId, Role.USER);
    }

    public static Accessor admin(Long userId) {
        return Accessor.of(userId, Role.ADMIN);
    }

    public boolean isUser() {
        return Role.USER.equals(role);
    }

    private static Accessor of(Long userId, Role role) {
        return Accessor.builder()
            .userId(userId)
            .role(role)
            .build();
    }
}
