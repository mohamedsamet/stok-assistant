package com.yesmind.stok.application.security.user;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserPrincipleToken extends AbstractAuthenticationToken {

    private final UserPrinciple userPrinciple;

    public UserPrincipleToken(UserPrinciple userPrinciple) {
        super(userPrinciple.getAuthorities());
        this.userPrinciple = userPrinciple;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;

    }

    @Override
    public UserPrinciple getPrincipal() {
        return userPrinciple;
    }
}
