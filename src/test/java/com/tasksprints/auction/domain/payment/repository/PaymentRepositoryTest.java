package com.tasksprints.auction.domain.payment.repository;

import com.tasksprints.auction.common.config.QueryDslConfig;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.model.PayStatus;
import com.tasksprints.auction.domain.payment.model.PayType;
import com.tasksprints.auction.domain.payment.model.Payment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;


@DataJpaTest
@Import(QueryDslConfig.class)
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Nested
    class 결제_저장_테스트 {
        @Test
        void 결제_응답이_올바르게_저장되면_성공한다() {
            //given
            PaymentResponse paymentResponseFromToss = PaymentResponse.builder()
                .payType("간편결제")
                .paymentKey("testPaymentKey")
                .amount(BigDecimal.ZERO)
                .orderName("testOrderName")
                .orderId("testOrderId")
                .status("DONE")
                .build();

            Payment payment = Payment.create(paymentResponseFromToss);
            //when
            Payment savedPayment = paymentRepository.save(payment);
            //then
            Assertions.assertThat(savedPayment.getPaymentId()).isNotNull();
            Assertions.assertThat(savedPayment.getPayType()).isEqualTo(PayType.SIMPLE_PAYMENT);
            Assertions.assertThat(savedPayment.getTossPaymentKey()).isEqualTo("testPaymentKey");
            Assertions.assertThat(savedPayment.getAmount()).isEqualTo(BigDecimal.ZERO);
            Assertions.assertThat(savedPayment.getPayStatus()).isEqualTo(PayStatus.DONE);

        }

        @Test
        void 결제_응답을_저장_시_create_메서드_처리_도중_ENUM에_없는_값이_들어오면_예외가_발생한다() {
            PaymentResponse paymentResponseFromToss = PaymentResponse.builder()
                .payType("잘못된 결제수단 입력")
                .paymentKey("testPaymentKey")
                .amount(BigDecimal.ZERO)
                .orderName("testOrderName")
                .orderId("testOrderId")
                .status("잘못된 결제 상태 입력")
                .build();

            Assertions.assertThatThrownBy(() -> Payment.create(paymentResponseFromToss))
                .isInstanceOf(IllegalArgumentException.class);

        }
    }

}
