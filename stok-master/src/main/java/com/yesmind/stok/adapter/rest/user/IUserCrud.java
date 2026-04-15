package com.yesmind.stok.adapter.rest.user;

import com.yesmind.stok.application.security.user.UserPrinciple;
import com.yesmind.stok.core.domain.data.UserRequest;
import org.springframework.http.ResponseEntity;

public interface IUserCrud {

    ResponseEntity<UserPrinciple> addUser(UserRequest userRequest);
    ResponseEntity<UserPrinciple> updatePassword(UserRequest userRequest);
}
