package com.tasksprints.auction.payment.exception;

public class PaymentDataMismatchException extends RuntimeException {
    public PaymentDataMismatchException(String message) {
        super(message);
    }
}
