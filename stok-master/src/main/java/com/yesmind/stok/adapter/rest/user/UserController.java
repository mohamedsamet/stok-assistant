package com.yesmind.stok.adapter.rest.user;

import com.yesmind.stok.application.security.user.UserPrinciple;
import com.yesmind.stok.core.domain.data.UserRequest;
import com.yesmind.stok.core.port.in.user.IUserFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements IUserCrud {

    private final IUserFactory userFactory;

    @Override
    @PostMapping("/user")
    public ResponseEntity<UserPrinciple> addUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userFactory.addUser(userRequest));
    }

    @Override
    @PutMapping("/user")
    public ResponseEntity<UserPrinciple> updatePassword(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userFactory.updateUser(userRequest));
    }
}
