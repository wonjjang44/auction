package com.tasksprints.auction.review.application.service;

import com.tasksprints.auction.review.domain.dto.request.ReviewRequest;
import com.tasksprints.auction.review.domain.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(Long userId, Long auctionId, ReviewRequest.Create review);

    List<ReviewResponse> getReviewsByUserId(Long userId);

    ReviewResponse getReviewByAuctionId(Long auctionId);

}
/**
 * 수정, 삭제 불가
 */
