package com.tasksprints.auction.auction.exception;

public class AuctionAlreadyClosedException extends RuntimeException {
    public AuctionAlreadyClosedException(String message) {
        super(message);
    }
}
