package com.tasksprints.auction.domain.payment.dto.response;

import com.tasksprints.auction.domain.payment.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class PaymentResponse {
    private String payType;
    private BigDecimal amount;
    private String orderName;
    private String orderId;
    private String customerEmail;
    private String customerName;
    private String successUrl;
    private String failUrl;



    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Detail {
        private Long paymentId;
        private String paymentKey;
        private String orderId;
        private String orderName;
        private BigDecimal amount;

        private String payStatus;
        private String payType;

        private String errorCode;
        private String errorMessage;
    }


    public static PaymentResponse.Detail of(Payment payment) {
        return Detail.builder()
            .paymentId(payment.getPaymentId())
            .paymentKey(payment.getTossPaymentKey())
            .orderId(payment.getTossOrderId())
            .orderName(payment.getOrderName())
            .amount(payment.getAmount())
            .payStatus(payment.getPayStatus().name())
            .payType(payment.getPayType().name())
            .build();
    }

}
