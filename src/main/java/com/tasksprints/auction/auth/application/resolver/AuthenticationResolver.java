package com.tasksprints.auction.auth.application.resolver;

import com.tasksprints.auction.common.jwt.Auth;
import com.tasksprints.auction.common.jwt.JwtProvider;
import com.tasksprints.auction.auth.infrastructure.TokenExtractor;
import com.tasksprints.auction.auth.exception.RefreshTokenException;
import com.tasksprints.auction.auth.domain.model.Accessor;
import com.tasksprints.auction.auth.application.RefreshTokenCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;
    private final RefreshTokenCookieManager refreshTokenCookieManager;
    private final TokenExtractor accessTokenExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter
            .hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new IllegalArgumentException();
        }
        try {
            String refreshToken = refreshTokenCookieManager.extractRefreshToken(request);
            String accessToken = accessTokenExtractor.extractToken(request);

            jwtProvider.validateToken(accessToken);
            jwtProvider.validateToken(refreshToken);

            Long userId = Long.valueOf(jwtProvider.getSubject(refreshToken));
            return Accessor.user(userId);
        } catch (RefreshTokenException e) {
            return Accessor.guest();
        }
    }
}
