package com.yesmind.stok.adapter.rest.auth;

import com.yesmind.stok.application.security.jwt.JwtIssuer;
import com.yesmind.stok.application.security.user.UserPrinciple;
import com.yesmind.stok.core.domain.data.LoginRequest;
import com.yesmind.stok.core.domain.data.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements IAuth {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authManager;

    @Override
    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest loginRequest) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrinciple principle = (UserPrinciple) authentication.getPrincipal();

        String token = jwtIssuer.issue(principle.getLogin(), principle.getPassword());
        return LoginResponse.builder()
                .accessToken(token)
                .build();

    }

    @Override
    @PostMapping("/auth/check")
    public boolean check(@RequestBody String token) {
        return jwtIssuer.check(token);
    }
}
