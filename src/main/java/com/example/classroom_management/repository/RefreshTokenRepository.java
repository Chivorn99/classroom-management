package com.example.classroom_management.repository;

import com.example.classroom_management.model.AppUser;
import com.example.classroom_management.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser_Username(String username);
    void deleteByUser(AppUser user);
}
