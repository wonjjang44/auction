package com.tasksprints.auction.bid.domain.entity;

import com.tasksprints.auction.common.entity.BaseEntity;
import com.tasksprints.auction.auction.domain.entity.Auction;
import com.tasksprints.auction.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "bids")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Bid extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //UUID를 ID로 사용하게 되면 성능 저하 -> 기본 키는 Id를 유지하되, 외부에 공개할 키는 UUID로 설정
    private String uuid;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    @Builder.Default
    private Auction auction = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private User user = null;

    public static Bid create(BigDecimal amount, User user, Auction auction) {
        Bid newBid = Bid.builder()
            .amount(amount)
            .uuid(UUID.randomUUID().toString())
            .build();
        newBid.addUserAndAuction(user, auction);
        return newBid;
    }

    /**
     * addUser 와 addAuction 을 양방향으로 묶을지 단방향으로 묶을지에 대한 고민
     */
    public void addUser(User user) {
        this.user = user;
    }

    public void addAuction(Auction auction) {
        this.auction = auction;
    }

    public void addUserAndAuction(User user, Auction auction) {
        addUser(user);
        addAuction(auction);
    }

    public void update(BigDecimal amount) {
        this.amount = amount;
    }
}
