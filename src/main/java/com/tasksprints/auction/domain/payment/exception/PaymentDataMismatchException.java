package com.tasksprints.auction.domain.payment.exception;

public class PaymentDataMismatchException extends RuntimeException {
    public PaymentDataMismatchException(String message) {
        super(message);
    }
}
