package com.tasksprints.auction.payment.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentErrorResponse {
    private String version;
    private String traceId;
    @JsonAlias({"code", "error.code"})
    private String code;
    @JsonAlias({"message", "error.message"})
    private String message;

}
