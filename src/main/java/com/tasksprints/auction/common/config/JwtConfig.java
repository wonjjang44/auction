package com.tasksprints.auction.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtConfig {

    @Value("${jwt.expire-ms}")
    private Long accessExpireMs;

    @Value("${jwt.expire-ms}")
    private Long refreshExpireMs;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secretKey;
}
