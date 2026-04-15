package com.yesmind.stok.application.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtIssuer implements IJwtIssuer {

    private final JwtProperties jwtProperties;

    @Override
    public String issue(String login, String password) {
        return JWT.create()
                .withSubject(login)
                .withClaim("pass", password)
                .withExpiresAt(Instant.now().plusSeconds(jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
    }

    @Override
    public boolean check(String token) {
        try {
            JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
