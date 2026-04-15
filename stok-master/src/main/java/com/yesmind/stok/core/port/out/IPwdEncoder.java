package com.yesmind.stok.core.port.out;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface IPwdEncoder {

    PasswordEncoder getEncoder();
}
