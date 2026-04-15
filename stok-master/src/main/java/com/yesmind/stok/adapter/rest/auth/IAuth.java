package com.yesmind.stok.adapter.rest.auth;

import com.yesmind.stok.core.domain.data.LoginRequest;
import com.yesmind.stok.core.domain.data.LoginResponse;

public interface IAuth {
    LoginResponse login(LoginRequest loginRequest);
    boolean check(String token);
}
