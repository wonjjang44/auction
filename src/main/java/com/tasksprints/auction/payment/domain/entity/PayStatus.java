package com.tasksprints.auction.payment.domain.entity;

import lombok.Getter;

@Getter
public enum PayStatus {
    READY("결제 생성 초기상태 - 결제 진행중"),
    IN_PROGRESS("결제 승인 대기중"),
    WAITING_FOR_DEPOSIT("가상 계좌 입금 대기중"),
    PARTIAL_CANCELED("승인된 결제 부분 취소"),
    EXPIRED("결제 유효 시간 초과"),
    DONE("결제 완료"),
    ABORTED("결제 승인 실패"),
    CANCELED("결제 취소"),
    FAILED("결제 실패");

    private final String displayName;

    PayStatus(String displayName) {this.displayName = displayName;}

    public static PayStatus fromString(String value) {
        for (PayStatus payStatus : PayStatus.values()) {
            if (payStatus.name().equalsIgnoreCase(value)) {
                return payStatus;
            }
        }
        throw new IllegalArgumentException("Unknown PayStatus: " + value);
    }
}
