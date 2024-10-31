package com.tasksprints.auction.domain.payment.service;


import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public interface PaymentService {

    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest);
    public HttpResponse<String> sendPaymentRequestToTossPayment(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException;
    public HttpResponse<String> requestPaymentApprovalCancellation(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException;
    public ResponseEntity<?> handleTossPaymentResponse(PaymentRequest.Confirm confirmRequest, HttpResponse<String> response) throws IOException, InterruptedException  ;
}
