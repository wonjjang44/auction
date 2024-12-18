package com.tasksprints.auction.common.properties;

import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "payment.toss")
@Getter
public class PaymentProperties {
    private String testClientApiKey;
    private String testSecretApiKey;
    private String successUrl;
    private String failUrl;

    public static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    public String getAuthorizations() {
        String encodedKey = Base64.getEncoder().encodeToString((testSecretApiKey + ":").getBytes());
        return "Basic " + encodedKey;
    }
}
