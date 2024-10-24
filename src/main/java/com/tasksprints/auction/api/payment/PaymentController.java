package com.tasksprints.auction.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.common.response.ApiResult;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.exception.InvalidSessionException;
import com.tasksprints.auction.domain.payment.exception.PaymentDataMismatchException;
import com.tasksprints.auction.domain.payment.service.PaymentService;
import com.tasksprints.auction.domain.user.repository.UserRepository;
import com.tasksprints.auction.domain.user.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentConfig paymentConfig;
    private final PaymentService paymentService;
    private final UserServiceImpl userService;
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final UserRepository userRepository;

    @PostMapping("/prepare")
    @Operation(summary = "Temporarily stores the payment element", description = "Save orderID and amount in session")
    @ApiResponse(responseCode = "200", description = "Payment prepared successfully")
    public ResponseEntity<ApiResult<String>> preparePayment(HttpSession session, @RequestBody PaymentRequest.Prepare prepareRequest) {
        paymentService.prepare(session, prepareRequest);
        return ResponseEntity.ok(ApiResult.success(ApiResponseMessages.PAYMENT_PREPARED_SUCCESS));
    }

    @Transactional
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(HttpSession session, @RequestBody PaymentRequest.Confirm confirmRequest) throws IOException, InterruptedException {
        //세션 유효 검증 및 결제 요청 전 후로 세션 값 변경 됐는지 검증
        validateSession(session);
        validatePaymentConfirmRequest(confirmRequest, session);

        //실제 토스페이먼츠로 결제 승인 요청
        HttpResponse<String> response = paymentService.requestConfirm(httpClient, confirmRequest);

        //토스 결제 승인 성공
        ObjectMapper objectMapper = new ObjectMapper();
        if (response.statusCode() == 200) {
            try {
                //토스에서 받은 Response의 Payment 객체를 Dto에 담기
                PaymentResponse paymentResponse = objectMapper.readValue(response.body(), PaymentResponse.class);
                //일단 , 하드 코딩 -> 나중에 jwt에서 userId 얻을 것
                Long userId = 1L;
                // UserDetailResponse foundUserDto = userService.getUserDetailsById(userId);
                String walletId = userService.getUserDetailsById(userId).getWalletId();

                //결제 정보를 지갑에 저장하고, 저장이 안 됐을 시에는 throw Exception


            } catch (Exception e) {
                //repo로 저장 실패 시에는 결제를 취소해야 함 -> 토스페이로 결제 취소 요청
            }
            //결제가 성공적, 결제 기록 저장했고 충전 로직까지 처리
        }

        //토스 결제 실패 -> 에러 메시지를 return | else 구문은 필요 없는데 일단 의미상 둠
        else {
            //클라이언트로 Response
        }
        return null;
    }

    private void validatePaymentConfirmRequest(PaymentRequest.Confirm confirmRequest, HttpSession session){
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
            //에러 처리
            throw new InvalidSessionException("Invalid session");
        }

    }





}
