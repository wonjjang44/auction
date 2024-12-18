package com.tasksprints.auction.common.jwt;

import java.nio.charset.StandardCharsets;

public class JwtUtil {

    /**
     * secret-key 를 인코딩 합니다.
     * */
    public static byte[] encodeSecretKey(String secretKey) {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }
}
