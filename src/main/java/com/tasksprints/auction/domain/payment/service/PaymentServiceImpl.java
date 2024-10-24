package com.tasksprints.auction.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.common.request.api.TossPaymentsHttpRequest;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentConfig paymentConfig;
    private final PaymentRepository paymentRepository;

    @Override
    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest) {
        session.setAttribute("orderId", prepareRequest.getOrderId());
        session.setAttribute("amount", prepareRequest.getAmount());
    }

    @Override
    public HttpResponse<String> requestConfirm(HttpClient httpClient, PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(confirmRequest);

        HttpRequest request = HttpRequest.newBuilder()
//            .uri(URI.create("<https://api.tosspayments.com/v1/payments/confirm>"))
            .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
            .header("Authorization", paymentConfig.getAuthorizations())
            .header("Content-Type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    @Override
    public Object detailPayments(PaymentRequest.Detail paymentRequest) throws IOException, InterruptedException {
        String url = "";

        if(paymentRequest != null && "".equals(paymentRequest.getPaymentKey())) {
            url += "orders/" + paymentRequest.getOrderId();
        } else if(paymentRequest != null && "".equals(paymentRequest.getOrderId())) {
            url += paymentRequest.getPaymentKey();
        }

        HttpResponse<?> response = TossPaymentsHttpRequest.requestTossPaymentsAPI(url, paymentConfig.getAuthorizations());
        return response.body();
    }
}
