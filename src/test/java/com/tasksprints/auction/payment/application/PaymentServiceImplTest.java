package com.tasksprints.auction.domain.payment.service;

import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.client.PaymentApiSerializer;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentErrorResponse;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.model.Payment;
import com.tasksprints.auction.domain.payment.repository.PaymentRepository;
import com.tasksprints.auction.domain.wallet.model.Wallet;
import com.tasksprints.auction.domain.wallet.service.WalletService;
import com.tasksprints.auction.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @Spy
    @InjectMocks
    private PaymentServiceImpl paymentService;
    @Mock
    private WalletService walletService;
    @Mock
    private PaymentApiSerializer paymentApiSerializer;
    @Mock
    private PaymentRepository paymentRepository;

    private MockHttpSession session;

    private User user;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();

        wallet = Wallet.builder()
            .id(1L)
            .balance(BigDecimal.ZERO)
            .userName("testUser")
            .build();

        user = User.builder()
            .id(1L)
            .name("testUser")
            .nickName("test")
            .password("password")
            .email("test@naver.com")
            .wallet(wallet)
            .build();


    }

    @Nested
    @DisplayName("결제 전 세션 임시 저장 테스트")
    class 임시_저장_테스트 {
        @Test
        void 결제_요청을_받았을_때_세션에_값이_저장되면_성공한다() {
            //given
            String orderId = "testOrderId";
            BigDecimal amount = BigDecimal.valueOf(1000.00);
            PaymentRequest.Prepare prepareRequest = new PaymentRequest.Prepare(orderId, amount);

            //when
            paymentService.prepare(session, prepareRequest);
            //then
            assertThat(session.getAttribute("orderId")).isEqualTo(orderId);
            assertThat(session.getAttribute("amount")).isEqualTo(amount);
        }

    }

    @Nested
    @DisplayName("토스_페이_응답_처리")
    class handleTossPayResponse {
        @Test
        void 결제_성공_시_지갑에_돈을_충전한다() throws IOException, InterruptedException {
            //given
            PaymentRequest.Confirm confirmRequest = new PaymentRequest.Confirm("orderId", BigDecimal.valueOf(50000), "paymentKey");
            PaymentResponse paymentResponse = PaymentResponse.builder()
                .payType("카드 결제")
                .paymentKey("paymentKey")
                .amount(BigDecimal.valueOf(50000))
                .orderName("Sample Order")
                .orderId("orderId")
                .status("DONE")
                .build();

            Response<Object> successResponse = Response.success(200, paymentResponse);

            when(walletService.getWalletByUserId(user.getId())).thenReturn(wallet);
            doNothing().when(walletService).chargeMoney(eq(wallet), eq(BigDecimal.valueOf(50000)));

            //when
            paymentService.handleTossPaymentResponse(1L, confirmRequest, successResponse);

            //then
            verify(walletService).chargeMoney(eq(wallet), eq(BigDecimal.valueOf(50000)));
            verify(paymentRepository).save(any(Payment.class));
        }

        @Test
        void 결제_성공_후_결제_정보_저장을_실패하면_결제_취소_요청을_보낸다() throws IOException, InterruptedException {
            // given
            PaymentRequest.Confirm confirmRequest = new PaymentRequest.Confirm("orderId", BigDecimal.valueOf(50000), "paymentKey");
            PaymentResponse paymentResponse = PaymentResponse.builder()
                .payType("카드 결제")
                .paymentKey("paymentKey")
                .amount(BigDecimal.valueOf(50000))
                .orderName("Sample Order")
                .orderId("orderId")
                .status("DONE")
                .build();

            Response<Object> successResponse = Response.success(200, paymentResponse);

            when(walletService.getWalletByUserId(user.getId())).thenReturn(wallet);
            when(paymentRepository.save(any(Payment.class)))
                .thenThrow(new RuntimeException("결제 정보 저장 실패"));

            // when
            assertThrows(RuntimeException.class, () ->
                paymentService.handleTossPaymentResponse(1L, confirmRequest, successResponse)
            );

            // then
            verify(paymentApiSerializer).cancelPaymentApproval(any(PaymentRequest.Cancel.class));
            //예외 발생으로 chargeMoney전에 throw 됐을 것
            verify(walletService, times(0)).chargeMoney(any(Wallet.class), any(BigDecimal.class));
        }

        @Test
        void 결제_실패_시_handlePaymentFailure_를_실행한다() throws IOException, InterruptedException {
            //given
            PaymentRequest.Confirm confirmRequest = new PaymentRequest.Confirm("orderId", BigDecimal.valueOf(50000), "paymentKey");
            PaymentErrorResponse errorResponse = PaymentErrorResponse.builder()
                .version("2022-11-16")
                .traceId("{traceId}")
                .code("{CODE}")
                .message("{MESSAGE}")
                .build();
            Response<Object> failureResponse = Response.failure(404, errorResponse);

            //when
            paymentService.handleTossPaymentResponse(1L, confirmRequest, failureResponse);

            //then
            verify(paymentService).handlePaymentFailure(failureResponse);
        }

    }


}
