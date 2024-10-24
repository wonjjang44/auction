package com.tasksprints.auction.domain.payment.exception;

public class PaymentDetailSearchFailException extends RuntimeException {
    public PaymentDetailSearchFailException(String message) {
        super(message);
    }
}
