package com.tasksprints.auction.domain.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Accessor {

    private static final Long GUEST_USER_ID = 0L;
    private final Long userId;
    private final Role role;

    public static Accessor guest() {
        return Accessor.of(GUEST_USER_ID, Role.GUEST);
    }
    public static Accessor user(Long userId) {
        return Accessor.of(userId, Role.USER);
    }
    public static Accessor admin(Long userId) {
        return Accessor.of(userId, Role.ADMIN);
    }

    private static Accessor of(Long userId, Role role) {
        return Accessor.builder()
            .userId(userId)
            .role(role)
            .build();
    }
}


