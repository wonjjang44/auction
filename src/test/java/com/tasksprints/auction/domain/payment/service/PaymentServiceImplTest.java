package com.tasksprints.auction.domain.payment.service;

import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

//    @Mock
//    private MockHttpSession MockSession;

    @Nested
    @DisplayName("결제 전 세션 임시 저장 테스트")
    class 임시_저장_테스트{
        @Test
        void 결제_요청을_받았을_때_세션에_값이_저장되면_성공한다 () {
        //given
        MockHttpSession session = new MockHttpSession();
        String orderId = "testOrderId";
        BigDecimal amount = BigDecimal.valueOf(1000.00);
        PaymentRequest.Prepare prepareRequest = new PaymentRequest.Prepare(orderId, amount);

        //when
        paymentService.prepare(session, prepareRequest);
        //then
        assertThat(session.getAttribute("orderId")).isEqualTo(orderId);
        assertThat(session.getAttribute("amount")).isEqualTo(amount);
        }
        @Test
        void 결제_요청을_받았을_때_세션에_값이_저장되지_않으면_예외_처리 () {
            //given
            //when
            //then
        }
    }
}
