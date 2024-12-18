package com.tasksprints.auction.payment.infrastructure.client;

import com.tasksprints.auction.payment.api.Response;
import com.tasksprints.auction.payment.domain.dto.request.PaymentRequest;

import java.io.IOException;

public interface PaymentApiSerializer {
    Response<Object> sendPaymentRequest(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException;
    Response<Object> cancelPaymentApproval(PaymentRequest.Cancel cancelRequest) throws IOException, InterruptedException;
}
