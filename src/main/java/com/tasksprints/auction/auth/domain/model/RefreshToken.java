package com.tasksprints.auction.auth.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String id;

    @Column
    private Long userId;

    public static RefreshToken of(String id, Long userId) {
        return RefreshToken.builder()
            .id(id)
            .userId(userId)
            .build();
    }
}
