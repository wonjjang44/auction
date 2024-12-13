package com.tasksprints.auction.bid.domain.entity.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class BidRequest {
    private Long userId;
    private Long auctionId;
    private BigDecimal amount;
}
