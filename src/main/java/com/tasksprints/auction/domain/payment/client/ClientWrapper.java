package com.tasksprints.auction.domain.payment.client;

import com.tasksprints.auction.domain.payment.api.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class ClientWrapper implements HttpClientWrapper{
    private final HttpClient httpClient;

    @Override
    public Response<String> send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Response.success(response.statusCode(), response.body());
        }
        return Response.failure(response.statusCode(), response.body());
    }
}
