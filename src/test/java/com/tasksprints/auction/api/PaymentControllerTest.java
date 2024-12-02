package com.tasksprints.auction.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.api.payment.PaymentController;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentErrorResponse;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import com.tasksprints.auction.domain.payment.exception.InvalidSessionException;
import com.tasksprints.auction.domain.payment.exception.PaymentDataMismatchException;
import com.tasksprints.auction.domain.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@MockBean(JpaMetamodelMappingContext.class)

public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    MockHttpSession session;

    @BeforeEach
    void setup() {
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("결제 전 임시 값 저장")
    public void 결제_전_임시_값_저장() throws Exception {
        String jsonRequest = """
            {
                "orderId": "test1",
                "amount": 1000.00
            }
            """;

        mockMvc.perform(post("/api/v1/payment/prepare")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(ApiResponseMessages.PAYMENT_PREPARED_SUCCESS));

    }

    @Nested
    class sessionTest {


        @Test
        void 결제_전_세션_값이_null인_경우_예외가_발생한다() throws Exception {
            // Given
            String jsonRequest = """
                {
                    "orderId": "12345",
                    "amount": 10000
                }
                """;

            // When & Then
            mockMvc.perform(post("/api/v1/payment/confirm")
                    .session(session)
                    .param("userId", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception resolvedException = result.getResolvedException();
                    assertNotNull(resolvedException);
                    assertInstanceOf(InvalidSessionException.class, resolvedException);
                });
        }

        @Test
        void 결제_전_세션_OrderId와_Request의_OrderId가_다른_경우_예외가_발생한다() throws Exception {
            // Given
            String jsonRequest = """
                {
                    "orderId": "12345",
                    "amount": 10000
                }
                """;
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("orderId", "changed-OrderId");
            session.setAttribute("amount", BigDecimal.valueOf(10000)); //

            // When & Then
            mockMvc.perform(post("/api/v1/payment/confirm")
                    .session(session)
                    .param("userId", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception resolvedException = result.getResolvedException();
                    assertNotNull(resolvedException);
                    assertInstanceOf(PaymentDataMismatchException.class, resolvedException);
                    assertEquals("Payment data mismatch", resolvedException.getMessage());

                });
        }
    }

    @Test
    @DisplayName("결제 승인 성공 시 HTTP 200 응답을 반환한다")
    void 결제_승인_성공_시_응답() throws Exception {
        // Given
        String jsonRequest = """
            {
                "orderId": "12345",
                "amount": 10000
            }
            """;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("orderId", "12345");
        session.setAttribute("amount", BigDecimal.valueOf(10000));

        PaymentResponse successPaymentResponse = new PaymentResponse("CARD", "paymentKey", BigDecimal.valueOf(10000), "Test Order", "12345", "DONE");
        Response<Object> mockResponse = Response.success(200, successPaymentResponse);

        when(paymentService.sendPaymentRequest(any())).thenReturn(mockResponse);
        when(paymentService.handleTossPaymentResponse(anyLong(), any(), any()))
            .thenReturn(mockResponse);

        // When / Then
        mockMvc.perform(post("/api/v1/payment/confirm")
                .session(session)
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("결제가 성공적으로 처리되었습니다."))
            .andExpect(jsonPath("$.data.orderId").value("12345"))
            .andExpect(jsonPath("$.data.totalAmount").value(10000));
    }

    @Test
    @DisplayName("결제 승인 성공 시 HTTP 400 응답을 반환한다")
    void 결제_승인_실패_시_응답() throws Exception {
        // Given
        String jsonRequest = """
            {
                "orderId": "12345",
                "amount": 10000
            }
            """;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("orderId", "12345");
        session.setAttribute("amount", BigDecimal.valueOf(10000));

        PaymentErrorResponse failurePaymentResponse = PaymentErrorResponse.builder()
            .version("2022-11-16")
            .traceId("{traceId}")
            .error(PaymentErrorResponse.ErrorDetail.builder()
                .code("{CODE}")
                .message("{MESSAGE}")
                .build()
            )
            .build();
        Response<Object> mockResponse = Response.failure(400, failurePaymentResponse);

        when(paymentService.sendPaymentRequest(any())).thenReturn(mockResponse);
        when(paymentService.handleTossPaymentResponse(anyLong(), any(), any()))
            .thenReturn(mockResponse);

        // When / Then
        mockMvc.perform(post("/api/v1/payment/confirm")
                .session(session)
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("{CODE}"))
            .andExpect(jsonPath("$.error.message").value("{MESSAGE}"));
    }

}
