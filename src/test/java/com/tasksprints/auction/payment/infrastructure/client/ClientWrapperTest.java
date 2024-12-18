package com.tasksprints.auction.domain.payment.client;

import com.tasksprints.auction.domain.payment.api.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientWrapperTest {
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> mockHttpResponse;
    @InjectMocks
    private ClientWrapper clientWrapper;

    @BeforeEach
    void setUp() {

    }

    @Test
    void send가_성공적으로_응답을_받음() throws IOException, InterruptedException {
        //given
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://example.com"))
            .build();

        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("test Body");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockHttpResponse);

        //when
        Response<String> result = clientWrapper.send(request);

        //then
        assertEquals(200, result.getStatusCode());
        assertEquals("test Body", result.getBody());
    }
}
