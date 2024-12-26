package com.tasksprints.auction.auction.domain.entity;

public enum AuctionStatus {
    PENDING,
    ACTIVE,
    CLOSED,
    CANCELED;

    public static AuctionStatus fromDisplayName(String auctionStatus) {
        for (AuctionStatus status : values() ) {
            if (status.name().equalsIgnoreCase(auctionStatus)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown auctionStatus: " + auctionStatus);
    }
}
