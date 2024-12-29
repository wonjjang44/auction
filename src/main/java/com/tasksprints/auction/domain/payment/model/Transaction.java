package com.tasksprints.auction.domain.payment.model;

import com.tasksprints.auction.common.entity.BaseEntity;
import com.tasksprints.auction.common.entity.BaseEntityWithUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Transaction extends BaseEntityWithUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String transactionKey;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayType payType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayStatus payStatus;



    public static Transaction create(String transactionKey, String paymentKey, String orderId, BigDecimal amount, PayType payType, PayStatus payStatus) {
        return Transaction.builder()
            .transactionKey(transactionKey)
            .paymentKey(paymentKey)
            .orderId(orderId)
            .amount(amount)
            .payType(payType)
            .payStatus(payStatus)
            .build();
    }

}
