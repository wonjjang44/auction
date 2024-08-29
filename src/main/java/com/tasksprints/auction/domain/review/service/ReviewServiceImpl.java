package com.tasksprints.auction.domain.review.service;

import com.tasksprints.auction.domain.auction.model.Auction;
import com.tasksprints.auction.domain.auction.repository.AuctionRepository;
import com.tasksprints.auction.domain.review.dto.ReviewDTO;
import com.tasksprints.auction.domain.review.dto.ReviewRequest;
import com.tasksprints.auction.domain.review.model.Review;
import com.tasksprints.auction.domain.review.repository.ReviewRepository;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    @Override
    public ReviewDTO createReview(Long userId, Long auctionId, ReviewRequest.Create review) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        Auction auction = auctionRepository.findAuctionById(auctionId)
                .orElseThrow();
        Review createdReview = Review.create(review.getContent(), review.getRating(), user, auction);
        Review savedReview = reviewRepository.save(createdReview);
        return ReviewDTO.of(savedReview);
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(ReviewDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewByAuctionId(Long auctionId) {
        Review foundReview = reviewRepository.findByAuctionId(auctionId)
                .orElseThrow();
        return ReviewDTO.of(foundReview);
    }
}