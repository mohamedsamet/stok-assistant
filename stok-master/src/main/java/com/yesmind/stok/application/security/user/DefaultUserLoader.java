package com.yesmind.stok.application.security.user;

import com.yesmind.stok.core.domain.data.UserRequest;
import com.yesmind.stok.core.port.in.user.IDefaultUserBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DefaultUserLoader {

    private final UserProperties userProperties;
    private final IDefaultUserBuilder userService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void addDefaultUser() {
        String defaultPassword = userProperties.getPwd();
        if (defaultPassword != null && !defaultPassword.startsWith("$argon2")) {
            defaultPassword = passwordEncoder.encode(defaultPassword);
        }

        userService.buildDefaultUser(UserRequest.builder()
                .login(userProperties.getLogin())
                .password(defaultPassword)
                .build());
    }

}
