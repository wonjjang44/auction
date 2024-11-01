package com.tasksprints.auction.domain.auth;

import static com.tasksprints.auction.common.constant.ApiResponseMessages.ACCESS_TOKEN_NOT_FOUND;

import com.tasksprints.auction.domain.auth.exception.AccessTokenException;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenExtractor implements TokenExtractor {
    private static final String TYPE = "Bearer ";

    public String extractToken(String header) {
        if(header != null && header.startsWith(TYPE)) {
            return header.substring(TYPE.length());
        }
        throw new AccessTokenException(ACCESS_TOKEN_NOT_FOUND);
    }
}
