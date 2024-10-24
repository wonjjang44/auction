package com.tasksprints.auction.domain.wallet.exception;

public class InSufficientBalanceException extends RuntimeException {
    public InSufficientBalanceException(String message) {
        super(message);
    }
}
