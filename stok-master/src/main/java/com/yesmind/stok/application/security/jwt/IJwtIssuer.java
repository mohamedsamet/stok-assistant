package com.yesmind.stok.application.security.jwt;

public interface IJwtIssuer {
    String issue(String login, String password);
    boolean check(String token);
}
