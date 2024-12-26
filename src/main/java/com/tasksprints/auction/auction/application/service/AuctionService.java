package com.tasksprints.auction.auction.application.service;

import com.tasksprints.auction.auction.domain.dto.request.AuctionRequest;
import com.tasksprints.auction.auction.domain.dto.response.AuctionResponse;
import com.tasksprints.auction.product.domain.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 사용자가 맞는지 판단도 해야함.
 */
public interface AuctionService {
    AuctionResponse createAuction(Long userId, AuctionRequest.Create auctionRequest);

    void closeAuction(Long auctionId);

    String getAuctionStatus(Long auctionId);

    List<AuctionResponse> getAuctionsByUser(Long userId);

    List<AuctionResponse> getAllAuctions();

    AuctionResponse getAuctionById(Long auctionId);

    Page<AuctionResponse.Details> getAuctionsByFilter(Pageable pageable, AuctionRequest.SearchCondition searchCondition);

    @Deprecated
    Page<AuctionResponse.Details> getAuctionsByProductCategory(Pageable pageable, AuctionRequest.SearchCondition searchCondition, ProductCategory category);
}
