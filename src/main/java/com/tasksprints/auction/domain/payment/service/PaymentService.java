package com.tasksprints.auction.domain.payment.service;


import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface PaymentService {

    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest);
    public Response<Object> sendPaymentRequest(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException;
    public Response<Object> handleTossPaymentResponse(Long userId, PaymentRequest.Confirm confirmRequest, Response<Object> response) throws IOException, InterruptedException  ;
}
