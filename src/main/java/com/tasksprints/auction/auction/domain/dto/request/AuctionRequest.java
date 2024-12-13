package com.tasksprints.auction.auction.domain.dto.request;

import com.tasksprints.auction.auction.domain.entity.AuctionCategory;
import com.tasksprints.auction.auction.domain.entity.AuctionStatus;
import com.tasksprints.auction.product.domain.entity.ProductCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private BigDecimal startingBid;
        private AuctionCategory auctionCategory;
        private AuctionStatus auctionStatus;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SearchCondition {
        private AuctionCategory auctionCategory;
        private ProductCategory productCategory;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private AuctionStatus auctionStatus;
        private String sortBy;

    }
}
