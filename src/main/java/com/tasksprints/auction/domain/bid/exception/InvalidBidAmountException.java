package com.tasksprints.auction.domain.bid.exception;

public class InvalidBidAmountException extends RuntimeException {
    public InvalidBidAmountException(String message) {
        super(message);
    }
}