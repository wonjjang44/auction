package com.tasksprints.auction.domain.payment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.common.config.PaymentConfig;
import com.tasksprints.auction.domain.payment.api.Response;
import com.tasksprints.auction.domain.payment.dto.request.PaymentRequest;
import com.tasksprints.auction.domain.payment.dto.response.PaymentErrorResponse;
import com.tasksprints.auction.domain.payment.dto.response.PaymentResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TossPaymentClientImplTest {

    @Mock
    private HttpClientWrapper httpClientWrapper;
    private PaymentConfig paymentConfig;
    private ObjectMapper objectMapper;
    private PaymentClient tossPaymentClient;

    @BeforeEach
    void setUp() {
        paymentConfig = new PaymentConfig() {
            @Override
            public String getAuthorizations() {
                return "https://api.test.com/payments";
            }
        };
        objectMapper = new ObjectMapper();
        tossPaymentClient = new TossPaymentClientImpl(paymentConfig, objectMapper, httpClientWrapper);

    }

    @Nested
    @DisplayName("결제 요청 테스트")
    class payment_request_test {
        @Test
        void payment_request의_응답이_200번_코드라면_PaymentResponse_타입을_리턴받는다() throws IOException, InterruptedException {
            //given
            PaymentRequest.Confirm confirmRequest = new PaymentRequest.Confirm("orderId", BigDecimal.TEN, "paymentKey");
            String responseBody = "{ \"status\": \"COMPLETED\", \"paymentKey\": \"abc123\",\"orderName\": \"Sample Order\", \"amount\": 5000 }";
            Response<String> mockResponse = Response.success(200, responseBody);

            when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(mockResponse);
            //when
            Response<Object> response = tossPaymentClient.sendPaymentRequest(confirmRequest);
            //then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
            Assertions.assertThat(response.getBody()).isInstanceOf(PaymentResponse.class);

        }

        @Test
        void payment_request의_응답이_200번_코드가_아니면_PaymentErrorResponse_타입을_리턴받는다() throws IOException, InterruptedException {
            //given
            PaymentRequest.Confirm confirmRequest = new PaymentRequest.Confirm("orderId", BigDecimal.TEN, "paymentKey");
            String responseBody = "{\"version\": \"2022-11-16\", \"traceId\": \"{traceId}\", " +
                "\"error\": {\"code\": \"{CODE}\", \"message\": \"{MESSAGE}\"}}";

            Response<String> mockResponse = Response.failure(404, responseBody);

            when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(mockResponse);
            //when
            Response<Object> response = tossPaymentClient.sendPaymentRequest(confirmRequest);
            //then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(404);
            Assertions.assertThat(response.getBody()).isInstanceOf(PaymentErrorResponse.class);

        }
    }

    @Nested
    @DisplayName("결제 취소 요청 테스트")
    class payment_cancel_test {
        @Test
        void cancel_request의_응답이_200번_코드라면_PaymentResponse_타입을_리턴받는다() throws IOException, InterruptedException {
            //given
            PaymentRequest.Cancel cancelRequest = new PaymentRequest.Cancel("paymentKey");
            String responseBody = "{ \"status\": \"CANCELED\", \"paymentKey\": \"abc123\",\"orderName\": \"Sample Order\", \"amount\": 5000 }";
            Response<String> mockResponse = Response.success(200, responseBody);

            when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(mockResponse);
            //when
            Response<Object> response = tossPaymentClient.cancelPaymentApproval(cancelRequest);
            //then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
            Assertions.assertThat(response.getBody()).isInstanceOf(PaymentResponse.class);

        }
        @Test
        void cancel_request의_응답이_200번이_아니라면_PaymentErrorResponse_타입을_리턴받는다() throws IOException, InterruptedException {
            //given
            PaymentRequest.Cancel cancelRequest = new PaymentRequest.Cancel("paymentKey");
            String responseBody = "{\"version\": \"2022-11-16\", \"traceId\": \"{traceId}\", " +
                            "\"error\": {\"code\": \"{CODE}\", \"message\": \"{MESSAGE}\"}}";
            Response<String> mockResponse = Response.success(400, responseBody);

            when(httpClientWrapper.send(any(HttpRequest.class))).thenReturn(mockResponse);
            //when
            Response<Object> response = tossPaymentClient.cancelPaymentApproval(cancelRequest);
            //then
            Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
            Assertions.assertThat(response.getBody()).isInstanceOf(PaymentErrorResponse.class);

        }

    }


}
