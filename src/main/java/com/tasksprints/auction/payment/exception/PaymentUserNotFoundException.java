package com.tasksprints.auction.payment.exception;

public class PaymentUserNotFoundException extends RuntimeException {
    public PaymentUserNotFoundException(String message) {
        super(message);
    }
}
