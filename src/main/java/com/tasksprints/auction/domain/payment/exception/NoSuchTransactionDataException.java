package com.tasksprints.auction.domain.payment.exception;

public class NoSuchTransactionDataException extends RuntimeException {
    public NoSuchTransactionDataException(String message) {
        super(message);
    }
}
