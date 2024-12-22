package com.tasksprints.auction.common.jwt;

import static com.tasksprints.auction.common.util.TimeUtil.*;

import com.tasksprints.auction.common.config.JwtConfig;
import com.tasksprints.auction.auth.domain.dto.response.AccessToken;
import com.tasksprints.auction.auth.domain.dto.response.UserTokens;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final String EMPTY_SUBJECT = "";
    private final JwtConfig jwtConfig;
    private final Clock clock;

    public UserTokens generateToken(String subject) {
        String accessTokenValue = createToken(subject, jwtConfig.getAccessExpireMs());
        AccessToken accessToken = AccessToken.of(accessTokenValue);

        String refreshToken = createToken(EMPTY_SUBJECT, jwtConfig.getRefreshExpireMs());

        return UserTokens.of(
            accessToken,
            refreshToken
        );
    }

    private String createToken(String subject, Long expiredMs) {
        byte[] secretKey = JwtUtil.encodeSecretKey(jwtConfig.getSecretKey());
        Date now = localDateTimeToDate(LocalDateTime.now(clock));
        Date expirationTime = new Date(now.getTime() + expiredMs);

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtConfig.getIssuer())
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expirationTime)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public void validateToken(String token) {
        parseToken(token);
    }

    public String getSubject(String token) {
        return parseToken(token)
            .getBody()
            .getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        byte[] secretKey = JwtUtil.encodeSecretKey(jwtConfig.getSecretKey());
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token);
    }
}
