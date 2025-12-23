package com.tasksprints.auction.bid.domain.dto;
import com.tasksprints.auction.bid.domain.entity.Bid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {
    Long userId;
    String name;
    Long auctionId;
    BigDecimal amount;
    String uuid;

    public static BidResponse of(Bid bid) {
        return BidResponse.builder()
            .userId(bid.getUser().getId())
            .name(bid.getUser().getName())
            .auctionId(bid.getAuction().getId())
            .amount(bid.getAmount())
            .uuid(bid.getUuid())
            .build();
        /** 아이템 목록 추가**/
    }
}
