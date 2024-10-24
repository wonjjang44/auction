package com.tasksprints.auction.domain.payment.dto.response;

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
}
