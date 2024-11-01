package com.tasksprints.auction.common.jwt;

import static com.tasksprints.auction.common.util.TimeUtil.*;

import com.tasksprints.auction.domain.auth.dto.response.UserTokens;
import io.jsonwebtoken.Claims;
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
    private final JwtConfig jwtConfig;
    private final Clock clock;

    public UserTokens generateToken(Long userId, String userRole) {
        return UserTokens.of(createAccessToken(userId, userRole), createRefreshToken());
    }

    public String createAccessToken(Long userId, String userRole) {

        Date now = localDateTimeToDate(LocalDateTime.now(clock));

        return Jwts.builder().setIssuer(jwtConfig.getIssuer()).claim("userId", userId).claim("userRole", userRole)
            .setIssuedAt(now).setExpiration(new Date(now.getTime() + jwtConfig.getExpireMs()))
            .signWith(SignatureAlgorithm.HS256, JwtUtil.encodeSecretKey(jwtConfig.getSecretKey())).compact();
    }

    public String createRefreshToken() {

        Date now = localDateTimeToDate(LocalDateTime.now(clock));

        return Jwts.builder().setIssuer(jwtConfig.getIssuer()).setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + jwtConfig.getRefreshExpireMs()))
            .signWith(SignatureAlgorithm.HS256, JwtUtil.encodeSecretKey(jwtConfig.getSecretKey())).compact();
    }

    public boolean verifyToken(String token) {

        Date now = localDateTimeToDate(LocalDateTime.now(clock));

        Claims claims = getClaims(token);

        return !claims.getExpiration().before(now);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(JwtUtil.encodeSecretKey(jwtConfig.getSecretKey()))
            .parseClaimsJws(token)
            .getBody();
    }
}
