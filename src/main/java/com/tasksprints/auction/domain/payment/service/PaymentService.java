package com.tasksprints.auction.domain.payment.service;


import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public interface PaymentService {

    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest);
    public HttpResponse<String> requestConfirm(HttpClient httpClient, PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException;

    Object detailPayments(PaymentRequest.Detail paymentRequest) throws IOException, InterruptedException;
}
