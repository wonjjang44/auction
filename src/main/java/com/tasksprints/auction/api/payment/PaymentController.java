package com.tasksprints.auction.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentErrorResponse;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.exception.InvalidSessionException;
import com.tasksprints.auction.domain.payment.exception.PaymentDataMismatchException;
import com.tasksprints.auction.domain.payment.exception.PaymentUserNotFoundException;
import com.tasksprints.auction.domain.payment.model.Payment;
import com.tasksprints.auction.domain.payment.repository.PaymentRepository;
import com.tasksprints.auction.domain.payment.service.PaymentService;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.repository.UserRepository;
import com.tasksprints.auction.domain.user.service.UserServiceImpl;
import com.tasksprints.auction.domain.wallet.model.Wallet;
import com.tasksprints.auction.domain.wallet.service.WalletServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/prepare")
    @Operation(summary = "Temporarily stores the payment element", description = "Save orderID and amount in session")
    @ApiResponse(responseCode = "200", description = "Payment prepared successfully")
    public ResponseEntity<ApiResult<String>> preparePayment(HttpSession session, @RequestBody PaymentRequest.Prepare prepareRequest) {
        paymentService.prepare(session, prepareRequest);
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.PAYMENT_PREPARED_SUCCESS));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(HttpSession session, @RequestBody PaymentRequest.Confirm confirmRequest, @RequestParam Long userId) throws IOException, InterruptedException {
        validateSession(session);
        validatePaymentConfirmRequest(confirmRequest, session);

        Response<Object> response = paymentService.sendPaymentRequest(confirmRequest);
        //토스페이먼츠로 보낸 결제 승인 요청에 대한 response 리턴
        Response<Object> objectResponse = paymentService.handleTossPaymentResponse(userId, confirmRequest, response);

        if (objectResponse.isSuccess()) {
            PaymentResponse paymentResponse = (PaymentResponse) objectResponse.getBody();
            return ResponseEntity.ok(ApiResult.success("결제가 성공적으로 처리되었습니다.", paymentResponse));
        }
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }


    private void validatePaymentConfirmRequest(PaymentRequest.Confirm confirmRequest, HttpSession session) {
        String savedOrderId = (String) session.getAttribute("orderId");
        BigDecimal savedAmount = (BigDecimal) session.getAttribute("amount");

        if (!confirmRequest.getOrderId().equals(savedOrderId) || !confirmRequest.getAmount().equals(savedAmount)) {
            throw new PaymentDataMismatchException("Payment data mismatch");
        }
    }

    private void validateSession(HttpSession session) {
        if (session == null) {
            throw new InvalidSessionException("Invalid session");
        }

        String savedOrderId = (String) session.getAttribute("orderId");
        BigDecimal savedAmount = (BigDecimal) session.getAttribute("amount");

        if (savedOrderId == null || savedAmount == null) {
            throw new InvalidSessionException("Invalid session");
        }
    }


}
