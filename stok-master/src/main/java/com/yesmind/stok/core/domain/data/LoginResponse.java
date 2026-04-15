package com.yesmind.stok.core.domain.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
}
