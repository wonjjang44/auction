package com.tasksprints.auction.common.jwt;

import static com.tasksprints.auction.common.util.TimeUtil.*;

import com.tasksprints.auction.domain.auth.dto.response.JwtResponse;
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

    private final JwtProperties jwtProperties;
    private final Clock clock;

    public JwtResponse generateToken(Long userId, String userRole) {
        return JwtResponse.of(createAccessToken(userId, userRole), createRefreshToken());
    }

    public String createAccessToken(Long userId, String userRole) {

        Date now = localDateTimeToDate(LocalDateTime.now(clock));

        return Jwts.builder().setIssuer(jwtProperties.getIssuer()).claim("userId", userId).claim("userRole", userRole)
            .setIssuedAt(now).setExpiration(new Date(now.getTime() + jwtProperties.getExpireMs()))
            .signWith(SignatureAlgorithm.HS256, JwtUtil.encodeSecretKey(jwtProperties.getSecretKey())).compact();
    }

    public String createRefreshToken() {

        Date now = localDateTimeToDate(LocalDateTime.now(clock));

        return Jwts.builder().setIssuer(jwtProperties.getIssuer()).setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + jwtProperties.getRefreshExpireMs()))
            .signWith(SignatureAlgorithm.HS256, JwtUtil.encodeSecretKey(jwtProperties.getSecretKey())).compact();
    }

    public boolean verifyToken(String token) {

        Date now = localDateTimeToDate(LocalDateTime.now(clock));

        Claims claims = getClaims(token);

        return !claims.getExpiration().before(now);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(JwtUtil.encodeSecretKey(jwtProperties.getSecretKey()))
            .parseClaimsJws(token)
            .getBody();
    }
}
