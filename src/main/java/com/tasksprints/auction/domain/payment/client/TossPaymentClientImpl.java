package com.tasksprints.auction.domain.payment.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentErrorResponse;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

@Component
@RequiredArgsConstructor
public class TossPaymentClientImpl implements PaymentClient {

    private final PaymentConfig paymentConfig;
    private final ObjectMapper objectMapper;
    private final HttpClientWrapper httpClientWrapper;

    @Override
    public Response<Object> sendPaymentRequest(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException {
        String requestBody = buildDtoToRequestBody(confirmRequest);
        HttpRequest request = createHttpRequest(requestBody);
        Response<String> response = paymentRequestToTossPay(request);

        if (response.isSuccess()) {
            PaymentResponse paymentResponse = objectMapper.readValue(response.getBody(), PaymentResponse.class);
            return Response.success(response.getStatusCode(), paymentResponse);
        }
        PaymentErrorResponse errorResponse = objectMapper.readValue(response.getBody(), PaymentErrorResponse.class);
        return Response.failure(response.getStatusCode(), errorResponse);
    }

    private String buildDtoToRequestBody(PaymentRequest.Confirm confirmRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(confirmRequest);
    }

    private HttpRequest createHttpRequest(String requestBody) {
        return HttpRequest.newBuilder()
            .uri(URI.create(PaymentConfig.CONFIRM_URL))
            .header("Authorization", paymentConfig.getAuthorizations())
            .header("Content-Type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
    }

    private Response<String> paymentRequestToTossPay(HttpRequest request) throws IOException, InterruptedException {
        return requestToTossPay(request);
    }

    @Override
    public Response<Object> cancelPaymentApproval(PaymentRequest.Cancel cancelRequest) throws IOException, InterruptedException {
        String requestBody = createCancelRequestBody();
        HttpRequest request = buildCancelHttpRequest(cancelRequest, requestBody);
        Response<String> response = requestToTossPay(request);

        if (response.isSuccess()) {
            PaymentResponse paymentResponse = objectMapper.readValue(response.getBody(), PaymentResponse.class);
            return Response.success(response.getStatusCode(), paymentResponse);
        }
        PaymentErrorResponse errorResponse = objectMapper.readValue(response.getBody(), PaymentErrorResponse.class);
        return Response.failure(response.getStatusCode(), errorResponse);

    }

    private String createCancelRequestBody() throws JsonProcessingException {
        ObjectNode object = objectMapper.createObjectNode();
        object.put("cancelReason", "결제 도중 오류 발생");
        return objectMapper.writeValueAsString(object);
    }

    private HttpRequest buildCancelHttpRequest(PaymentRequest.Cancel cancelRequest, String requestBody) {
        String insertPaymentKeyIntoUrl = String.format("https://api.tosspayments.com/v1/payments/%s/cancel", cancelRequest.getPaymentKey());
        return HttpRequest.newBuilder()
            .uri(URI.create(insertPaymentKeyIntoUrl))
            .header("Authorization", paymentConfig.getAuthorizations())
            .header("Content-Type", "application/json")
            //            .header("Idempotency-key", "멱등키")
            .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
    }

    private Response<String> requestToTossPay(HttpRequest request) throws IOException, InterruptedException {
        return httpClientWrapper.send(request);
    }

}
