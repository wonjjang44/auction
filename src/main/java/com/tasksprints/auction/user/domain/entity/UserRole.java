package com.tasksprints.auction.user.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private final String userRole;
}
