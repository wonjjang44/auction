package com.tasksprints.auction.review.application.service;

import com.tasksprints.auction.auction.domain.entity.Auction;
import com.tasksprints.auction.auction.infrastructure.AuctionRepository;
import com.tasksprints.auction.review.domain.dto.request.ReviewRequest;
import com.tasksprints.auction.review.domain.dto.response.ReviewResponse;
import com.tasksprints.auction.review.domain.entity.Review;
import com.tasksprints.auction.review.infrastructure.ReviewRepository;
import com.tasksprints.auction.user.domain.entity.User;
import com.tasksprints.auction.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    @Override
    public ReviewResponse createReview(Long userId, Long auctionId, ReviewRequest.Create review) {
        User user = userRepository.findById(userId)
            .orElseThrow();
        Auction auction = auctionRepository.findAuctionById(auctionId)
            .orElseThrow();
        Review createdReview = Review.create(review.getContent(), review.getRating(), user, auction);
        Review savedReview = reviewRepository.save(createdReview);
        return ReviewResponse.of(savedReview);
    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
            .map(ReviewResponse::of)
            .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse getReviewByAuctionId(Long auctionId) {
        Review foundReview = reviewRepository.findByAuctionId(auctionId)
            .orElseThrow();
        return ReviewResponse.of(foundReview);
    }
}
