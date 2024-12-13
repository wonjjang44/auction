package com.tasksprints.auction.auction.infrastructure.support;

import com.tasksprints.auction.auction.domain.dto.request.AuctionRequest;
import com.tasksprints.auction.auction.domain.entity.Auction;

import com.tasksprints.auction.product.domain.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AuctionCriteriaRepository {
    Page<Auction> getAuctionsByFilters(Pageable pageable, AuctionRequest.SearchCondition searchCondition);
    @Deprecated
    Page<Auction> getAuctionsByCategory(Pageable pageable,
                                                AuctionRequest.SearchCondition searchCondition,
                                                ProductCategory category);
}
