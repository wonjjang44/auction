package com.tasksprints.auction.review.domain.dto.request;

import lombok.Data;

public class ReviewRequest {
    @Data
    public static class Create {
        Integer rating;
        String content;
    }
}
