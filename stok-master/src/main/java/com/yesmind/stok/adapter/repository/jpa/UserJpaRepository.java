package com.yesmind.stok.adapter.repository.jpa;

import com.yesmind.stok.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Override
    User save(User user);
}
