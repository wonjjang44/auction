package com.tasksprints.auction.wallet.exception;

public class InSufficientBalanceException extends RuntimeException {
    public InSufficientBalanceException(String message) {
        super(message);
    }
}
