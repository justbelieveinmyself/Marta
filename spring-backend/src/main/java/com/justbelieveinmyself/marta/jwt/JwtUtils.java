package com.justbelieveinmyself.marta.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.justbelieveinmyself.marta.domain.dto.auth.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Duration jwtLifetime;

    public AccessToken createAccessToken(String username) {
        Instant instant = Instant.ofEpochMilli(ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli() + jwtLifetime.toMillis());
        String token  = JWT.create()
                .withSubject(username)
                .withExpiresAt(instant)
                .sign(Algorithm.HMAC256(secret));
        return new AccessToken(instant, token);
    }
}
