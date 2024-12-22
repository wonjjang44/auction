package com.tasksprints.auction.review.domain.dto.response;

import com.tasksprints.auction.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    Integer rating;
    String content;

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
            .rating(review.getRating())
            .content(review.getContent())
            .build();
    }
}
