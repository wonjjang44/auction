package com.tasksprints.auction.payment.infrastructure.client;

import com.tasksprints.auction.payment.api.Response;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface HttpClientWrapper {
    Response<String> send(HttpRequest request) throws IOException, InterruptedException;
}
