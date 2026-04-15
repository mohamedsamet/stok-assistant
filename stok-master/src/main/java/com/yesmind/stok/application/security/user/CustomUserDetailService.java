package com.yesmind.stok.application.security.user;

import com.yesmind.stok.core.domain.entity.User;
import com.yesmind.stok.core.port.in.user.IUserLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final IUserLoader userLoader;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userLoader.getUser(username)
                .orElseThrow();

        return UserPrinciple.builder()
                .login(username)
                .password(user.getPassword())
                .userPublicId(user.getPublicId().toString())
                .build();
    }


}
