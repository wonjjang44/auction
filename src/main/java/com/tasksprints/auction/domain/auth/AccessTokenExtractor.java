package com.tasksprints.auction.domain.auth;

import static com.tasksprints.auction.common.constant.ApiResponseMessages.ACCESS_TOKEN_NOT_FOUND;

import com.tasksprints.auction.domain.auth.exception.AccessTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component

public class AccessTokenExtractor implements TokenExtractor {
    private static final String TYPE = "Bearer ";
    private static final String HEADER = "Authorization";

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if(header != null && header.startsWith(TYPE)) {
            return header.substring(TYPE.length());
        }
        throw new AccessTokenException(ACCESS_TOKEN_NOT_FOUND);
    }
}
