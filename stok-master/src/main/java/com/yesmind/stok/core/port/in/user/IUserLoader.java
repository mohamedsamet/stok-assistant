package com.yesmind.stok.core.port.in.user;

import com.yesmind.stok.core.domain.entity.User;

import java.util.Optional;

public interface IUserLoader {

    Optional<User> getUser(String login);
}
