package com.tasksprints.auction.domain.payment.model;

import lombok.Getter;

@Getter
public enum PayType {
    CARD("카드 결제"),
    VIRTUAL_ACCOUNT("가상계좌"),
    SIMPLE_PAYMENT("간편결제"),
    MOBILE_PAYMENT("휴대폰"),
    BANK_TRANSFER("계좌 이체"),
    CULTURE_GIFT_CERTIFICATE("문화상품권"),
    BOOK_CULTURE_CERTIFICATE("도서문화상품권"),
    GAME_CULTURE_CERTIFICATE("게임문화상품권");


    private final String displayName;

    PayType(String displayName) {
        this.displayName = displayName;
    }


    public static PayType fromString(String value) {
        for (PayType type : PayType.values()) {
            if (type.displayName.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PayType: " + value);
    }
}
