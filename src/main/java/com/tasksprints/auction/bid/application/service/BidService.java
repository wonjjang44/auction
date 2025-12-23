package com.tasksprints.auction.bid.application.service;

import com.tasksprints.auction.bid.domain.dto.BidResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    /**
     * 입찰
     * 입찰금액 변경
     */
    BidResponse submitBid(Long userId, Long auctionId, BigDecimal amount);

    BidResponse updateBidAmount(Long userId, Long auctionId, BigDecimal amount);

    Boolean hasUserAlreadyBid(Long auctionId);

    BidResponse getBidByUuid(String uuid);

    boolean isBidEnd(Long auctionId);

    List<BidResponse> findAllBids();
}
