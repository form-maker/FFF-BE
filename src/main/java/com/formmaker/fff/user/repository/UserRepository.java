package com.formmaker.fff.user.repository;

import com.formmaker.fff.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByLoginId(String loginId);
}
