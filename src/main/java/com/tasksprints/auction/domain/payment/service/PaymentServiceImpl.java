package com.tasksprints.auction.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.common.request.api.TossPaymentsHttpRequest;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.request.TransactionRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.dto.response.TransactionResponse;
import com.tasksprints.auction.domain.payment.exception.NoSuchTransactionDataException;
import com.tasksprints.auction.domain.payment.exception.PaymentDetailSearchFailException;
import com.tasksprints.auction.domain.payment.exception.PaymentWrongParameterException;
import com.tasksprints.auction.domain.payment.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
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
            .uri(URI.create("<https://api.tosspayments.com/v1/payments/confirm>"))
            .header("Authorization", paymentConfig.getAuthorizations())
            .header("Content-Type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    @Override
    public PaymentResponse.Detail detailPayments(PaymentRequest.Detail paymentRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = "";

        if(paymentRequest != null && "".equals(paymentRequest.getPaymentKey())) {
            url += "payments/orders/" + paymentRequest.getOrderId();
        } else if(paymentRequest != null && "".equals(paymentRequest.getOrderId())) {
            url += "payments/" + paymentRequest.getPaymentKey();
        } else {
            throw new PaymentWrongParameterException("paymentKey 또는 orderId 둘중 하나의 값만 올 수 있습니다.");
        }

        PaymentResponse.Detail readValue;

        try {
            HttpResponse<?> response = TossPaymentsHttpRequest.requestTossPaymentsAPI(url, paymentConfig.getAuthorizations());
            String jsonData = String.valueOf(response.body());

            log.debug("jsonData = {}", jsonData);

            readValue = objectMapper.readValue(jsonData, PaymentResponse.Detail.class);
        } catch (IOException | InterruptedException e) {
            log.debug("Exception = {}", e.getMessage());

            throw new PaymentDetailSearchFailException("결제 상세 정보 조회 실패");
        }

        return readValue;
    }


    @Override
    public TransactionResponse getTransactionList(TransactionRequest transactionRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = "transactions?startDate=" + transactionRequest.getStartDate() + "&endDate=" + transactionRequest.getEndDate();

        log.debug("transactionRequest = {}", transactionRequest);

        TransactionResponse readValue;

        try {
            HttpResponse<?> response = TossPaymentsHttpRequest.requestTossPaymentsAPI(url, paymentConfig.getAuthorizations());
            String jsonData = String.valueOf(response.body());

            if(jsonData.equals("[]"))
                return new TransactionResponse();

            readValue = objectMapper.readValue(jsonData, TransactionResponse.class);

        } catch (IOException | InterruptedException | NoSuchTransactionDataException e) {
            log.debug("Exception => {}", e.getMessage());

            throw new NoSuchTransactionDataException("거래 조회 실패");
        }

        return readValue;
    }
}
