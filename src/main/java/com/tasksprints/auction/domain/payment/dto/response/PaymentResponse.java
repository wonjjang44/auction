package com.tasksprints.auction.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import java.math.BigDecimal;
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
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
