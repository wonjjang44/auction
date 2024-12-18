package com.tasksprints.auction.common.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {
    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.expire-ms}")
    private Long expireMs;

    @Value("${jwt.expire-ms}")
    private Long refreshExpireMs;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secretKey;
}
