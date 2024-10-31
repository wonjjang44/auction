package com.tasksprints.auction.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.model.Payment;
import com.tasksprints.auction.domain.payment.repository.PaymentRepository;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.service.UserServiceImpl;
import com.tasksprints.auction.domain.wallet.model.Wallet;
import com.tasksprints.auction.domain.wallet.service.WalletServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final WalletServiceImpl walletService;
    private final UserServiceImpl userService;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final Long FIXED_USER_ID = 2L;

    @Override
    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest) {
        session.setAttribute("orderId", prepareRequest.getOrderId());
        session.setAttribute("amount", prepareRequest.getAmount());
    }

    @Override
    public HttpResponse<String> sendPaymentRequestToTossPayment(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(confirmRequest);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(PaymentConfig.CONFIRM_URL))
            .header("Authorization", paymentConfig.getAuthorizations())
            .header("Content-Type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<?> handleTossPaymentResponse(PaymentRequest.Confirm confirmRequest, HttpResponse<String> response) throws IOException, InterruptedException {
        if (response.statusCode() == 200) {
            return handlePaymentSuccess(confirmRequest, response);
        }
        return handlePaymentFailure(response);
    }

    private ResponseEntity<ApiResult<PaymentResponse>> handlePaymentSuccess(PaymentRequest.Confirm confirmRequest, HttpResponse<String> response) throws IOException, InterruptedException {
        //결제에 성공했더라도 결제 정보 저장에 실패하면 트랜잭션 롤백한다
        try {
            PaymentResponse paymentResponseFromToss = objectMapper.readValue(response.body(), PaymentResponse.class);
            Payment payment = Payment.create(paymentResponseFromToss);

            Wallet paymentUserWallet = this.getWalletFromUserId(FIXED_USER_ID);
            payment.addWallet(paymentUserWallet);

            paymentRepository.save(payment);
            walletService.chargeMoney(paymentUserWallet, payment.getAmount());

            log.info("결제 성공.. 충전!");
            return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.PAYMENT_SUCCESS, paymentResponseFromToss));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            requestPaymentApprovalCancellation(confirmRequest);
            log.info("결제 정보 저장 실패, 토스페이로 결제 취소 요청");
            throw e;
        }
    }

    private ResponseEntity<String> handlePaymentFailure(HttpResponse<String> response) {
        log.info("결제 실패");
        return ResponseEntity.status(response.statusCode()).body(response.body());
    }

    @Override
    public HttpResponse<String> requestPaymentApprovalCancellation(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException {
        ObjectNode object = objectMapper.createObjectNode();
        object.put("cancelReason", "결제 도중 오류 발생");

        String requestBody = objectMapper.writeValueAsString(object);
        String insertPaymentKeyIntoUrl = String.format("https://api.tosspayments.com/v1/payments/%s/cancel", confirmRequest.getPaymentKey());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(insertPaymentKeyIntoUrl))
            .header("Authorization", paymentConfig.getAuthorizations())
            .header("Content-Type", "application/json")
//            .header("Idempotency-key", "멱등키")
            .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Wallet getWalletFromUserId(Long userId) {
        User paymentUser = userService.findUserById(userId);
        return paymentUser.getWallet();
    }

}
