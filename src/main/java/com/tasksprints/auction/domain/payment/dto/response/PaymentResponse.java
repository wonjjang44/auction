package com.tasksprints.auction.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tasksprints.auction.domain.payment.model.Payment;
import lombok.*;

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
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true) // JSON 필드 중 DTO에 없는 필드를 무시한다
    public static class Detail {
        private Long paymentId;
        private String paymentKey;
        private String orderId;
        private String orderName;
        @JsonProperty("totalAmount")
        private BigDecimal amount;

        @JsonProperty("status")
        private String payStatus;

        @JsonProperty("method")
        private String payType;

        private String errorCode;
        private String errorMessage;

        private String requestedAt;
        private String approvedAt;
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
