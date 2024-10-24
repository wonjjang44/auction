package com.tasksprints.auction.domain.payment.model;

import lombok.Getter;

@Getter
public enum PayType {
    CARD("카드 결제"),
    BANK_TRANSFER("계좌 이체"),
    MOBILE_PAYMENT("모바일 결제");

    private final String payType;

    PayType(String payType) {
        this.payType = payType;
    }

    public static PayType fromString(String type) {
        for (PayType payType : PayType.values()) {
            if (payType.name().equalsIgnoreCase(type)) {
                return payType;
            }
        }
        throw new IllegalArgumentException("Unknown PayType: " + type);
    }
}
