package com.tasksprints.auction.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    @JsonProperty("method")
    private String payType;
    private String paymentKey;
    @JsonProperty("totalAmount")
    private BigDecimal amount;
    private String orderName;
    private String orderId;
    private String status;

}
