package com.yesmind.stok.core.port.in.user;

import com.yesmind.stok.core.domain.data.UserRequest;

public interface IDefaultUserBuilder {

    void buildDefaultUser(UserRequest userRequest);
}
