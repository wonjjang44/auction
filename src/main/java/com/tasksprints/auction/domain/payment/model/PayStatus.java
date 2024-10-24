package com.tasksprints.auction.domain.payment.model;

import lombok.Getter;

@Getter
public enum PayStatus {
    PENDING("결제 진행중"),
    APPROVED("결제 완료"),
    CANCELED("결제 취소"),
    FAILED("결제 실패");

    private final String status;

    PayStatus(String status) {this.status = status;}

    public static PayStatus fromString(String status) {
        for (PayStatus payStatus : PayStatus.values()) {
            if (payStatus.name().equalsIgnoreCase(status)) {
                return payStatus;
            }
        }
        throw new IllegalArgumentException("Unknown PayStatus: " + status);
    }
}
