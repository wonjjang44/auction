package com.tasksprints.auction.payment.application.service;

import com.tasksprints.auction.payment.api.Response;
import com.tasksprints.auction.payment.infrastructure.client.PaymentApiSerializer;
import com.tasksprints.auction.payment.domain.dto.request.PaymentRequest;
import com.tasksprints.auction.payment.domain.dto.response.PaymentResponse;
import com.tasksprints.auction.payment.domain.entity.Payment;
import com.tasksprints.auction.payment.infrastructure.PaymentRepository;
import com.tasksprints.auction.wallet.domain.entity.Wallet;
import com.tasksprints.auction.wallet.application.service.WalletService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final WalletService walletService;
    private final PaymentApiSerializer paymentApiSerializer;

    @Override
    public void prepare(HttpSession session, PaymentRequest.Prepare prepareRequest) {
        session.setAttribute("orderId", prepareRequest.getOrderId());
        session.setAttribute("amount", prepareRequest.getAmount());
    }
    @Override
    public Response<Object> sendPaymentRequest(PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException{
        return paymentApiSerializer.sendPaymentRequest(confirmRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response<Object> handleTossPaymentResponse(Long userId, PaymentRequest.Confirm confirmRequest, Response<Object> response) throws IOException, InterruptedException {
        if (response.getStatusCode() == 200) {
            return handlePaymentSuccess(userId, confirmRequest, response);
        }
        return handlePaymentFailure(response);
    }

    private Response<Object> handlePaymentSuccess(Long userId, PaymentRequest.Confirm confirmRequest, Response<Object> response) throws IOException, InterruptedException {
        //결제에 성공했더라도 결제 정보 저장에 실패하면 트랜잭션 롤백한다
        try {
            PaymentResponse paymentResponseFromToss = (PaymentResponse) response.getBody();
            Payment payment = Payment.create(paymentResponseFromToss);

            Wallet paidUserWallet = walletService.getWalletByUserId(userId);
            payment.addWallet(paidUserWallet);

            paymentRepository.save(payment);
            walletService.chargeMoney(paidUserWallet, payment.getAmount());
            return response;
        } catch (Exception e) {
            log.error("결제 처리 중 예외 발생: {}", e.getMessage(), e);
            String paymentKey = confirmRequest.getPaymentKey();
            PaymentRequest.Cancel cancelRequest = new PaymentRequest.Cancel(paymentKey);

            paymentApiSerializer.cancelPaymentApproval(cancelRequest);
            throw e;
        }
    }

    public Response<Object> handlePaymentFailure(Response<Object> response) {
        log.info("결제 실패");
        return response;
    }
}
