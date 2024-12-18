package com.tasksprints.auction.domain.payment.api;

import lombok.Getter;

@Getter
public class Response<T> {
    private final int statusCode;
    private final T body;

    public Response(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;

    }

    public static <T> Response<T> success(int statusCode, T body) {
        return new Response<>(statusCode, body);
    }

    public static <T> Response<T> failure(int statusCode, T body) {
        return new Response<>(statusCode, body);
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }

}
