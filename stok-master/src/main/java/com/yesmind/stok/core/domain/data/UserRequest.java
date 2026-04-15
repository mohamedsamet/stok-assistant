package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.adapter.validation.ValidPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class UserRequest {

    @NonNull
    private String login;

    @NonNull
    @ValidPassword
    private String password;
}
