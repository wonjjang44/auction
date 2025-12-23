package com.tasksprints.auction.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private Long accessExpireMs;

    private Long refreshExpireMs;

    private String issuer;

    private String secretKey;
}
