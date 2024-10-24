package com.tasksprints.auction.domain.payment.model;

import com.tasksprints.auction.common.entity.BaseEntityWithUpdate;
import com.tasksprints.auction.domain.wallet.model.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntityWithUpdate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false, unique = true)
    private String tossPaymentKey;

    @Column(nullable = false)
    private String tossOrderId;

    @Column(nullable = false)
    private String orderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = true)
    private String failReason;

    @Column(nullable = true)
    private String cancelReason;

    public void addWallet(Wallet wallet) {
        this.wallet = wallet;
        wallet.addPayment(this);
    }
}
