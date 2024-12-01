package com.tasksprints.auction.domain.payment.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface PaymentClient {
    Response<Object> sendPaymentRequest(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException;
    Response<Object> cancelPaymentApproval(PaymentRequest.Cancel cancelRequest) throws IOException, InterruptedException;
}
