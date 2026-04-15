package com.yesmind.stok.core.domain.data;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class LoginRequest {

    @NonNull
    private String login;

    @NonNull
    private String password;
}
