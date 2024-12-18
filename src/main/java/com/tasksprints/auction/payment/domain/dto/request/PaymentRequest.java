package com.tasksprints.auction.payment.domain.dto.request;

import com.tasksprints.auction.payment.domain.entity.PayType;
import com.tasksprints.auction.payment.domain.entity.Payment;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private BigDecimal amount;
    private String orderId;
    private String orderName;
    private PayType payType;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Prepare {
        private String orderId;
        private BigDecimal amount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Confirm {
        private String orderId;
        private BigDecimal amount;
        private String paymentKey;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Cancel {
        private String paymentKey;
    }

    public Payment toEntity() {
        return Payment.builder()
            .amount(amount)
            .tossOrderId(orderId)
            .orderName(orderName)
            .payType(payType)
            .build();
    }


}
