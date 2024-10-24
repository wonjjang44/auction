package com.tasksprints.auction.domain.payment.dto.request;

import com.tasksprints.auction.domain.payment.model.PayType;
import com.tasksprints.auction.domain.payment.model.Payment;
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
//    private String successUrl;
//    private String failUrl;

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
        private String tossPaymentKey;
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
