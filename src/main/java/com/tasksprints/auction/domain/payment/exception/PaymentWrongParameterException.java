package com.tasksprints.auction.domain.payment.exception;

public class PaymentWrongParameterException extends RuntimeException {
    public PaymentWrongParameterException(String message) {
        super(message);
    }
}
