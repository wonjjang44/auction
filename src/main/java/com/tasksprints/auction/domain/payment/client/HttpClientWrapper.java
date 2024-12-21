package com.tasksprints.auction.domain.payment.client;

import com.tasksprints.auction.domain.payment.api.Response;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface HttpClientWrapper {
    Response<String> send(HttpRequest request) throws IOException, InterruptedException;
}
