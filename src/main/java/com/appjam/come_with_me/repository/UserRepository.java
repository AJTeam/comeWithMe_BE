package com.appjam.come_with_me.repository;

import com.appjam.come_with_me.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByProviderId(String providerId);

    Optional<User> getUserByToken(String token);
}
