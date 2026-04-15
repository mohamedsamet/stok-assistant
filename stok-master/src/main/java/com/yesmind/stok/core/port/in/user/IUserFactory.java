package com.yesmind.stok.core.port.in.user;

import com.yesmind.stok.application.security.user.UserPrinciple;
import com.yesmind.stok.core.domain.data.UserRequest;

public interface IUserFactory {

    UserPrinciple addUser(UserRequest userRequest);
    UserPrinciple updateUser(UserRequest userRequest);
}
