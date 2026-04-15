package com.yesmind.stok.application.security.jwt.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yesmind.stok.application.security.jwt.JwtProperties;
import com.yesmind.stok.application.security.user.UserPrinciple;
import com.yesmind.stok.application.security.user.UserPrincipleToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            extractToken(request)
                    .map(this::decode)
                    .map(this::convert)
                    .map(UserPrincipleToken::new)
                    .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenPrefix = "Bearer ";
        if (StringUtils.hasText(token)) {
            return Optional.of(token.substring(tokenPrefix.length()));
        }
        return Optional.empty();
    }

    public UserPrinciple convert(DecodedJWT jwt) {
        return UserPrinciple.builder()
                .login(jwt.getSubject())
                .build();
    }

    public DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                .build()
                .verify(token);
    }
}
