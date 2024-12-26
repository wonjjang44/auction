package com.tasksprints.auction.bid.exception;

public class InvalidBidAmountException extends RuntimeException {
    public InvalidBidAmountException(String message) {
        super(message);
    }
}
