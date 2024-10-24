package com.tasksprints.auction.common.request.api;

import com.tasksprints.auction.common.config.PaymentConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class TossPaymentsHttpRequest {

    public static HttpResponse<?> requestTossPaymentsAPI(String param, String secretKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(PaymentConfig.URL + param))
            .header("Authorization", secretKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

}
