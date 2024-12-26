package com.tasksprints.auction.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.expire-ms}")
    private final Long accessExpireMs;

    @Value("${jwt.expire-ms}")
    private final Long refreshExpireMs;

    @Value("${jwt.issuer}")
    private final String issuer;

    @Value("${jwt.secret}")
    private final String secretKey;
}
