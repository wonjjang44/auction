package com.tasksprints.auction.domain.payment.exception;

public class PaymentUserNotFoundException extends RuntimeException {
    public PaymentUserNotFoundException(String message) {
        super(message);
    }
}
