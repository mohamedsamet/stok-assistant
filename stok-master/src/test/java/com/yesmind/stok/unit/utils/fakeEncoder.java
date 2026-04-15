package com.yesmind.stok.unit.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class fakeEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return "fake_pass";
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
