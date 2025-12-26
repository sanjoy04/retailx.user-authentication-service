package com.retailx.user_authentication_service.repository;

import com.retailx.user_authentication_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Used by loadUserByUsername (in CustomUserDetailsService) and login
    Optional<User> findByUsername(String username);

    // Used during registration to prevent duplicate usernames
    boolean existsByUsername(String username);

    // Used during registration to prevent duplicate emails
    boolean existsByEmail(String email);
}
