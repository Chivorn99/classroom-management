package com.example.classroom_management.service;

import com.example.classroom_management.model.AppUser;
import com.example.classroom_management.model.RefreshToken;
import com.example.classroom_management.repository.AppUserRepository;
import com.example.classroom_management.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository userRepository;

    @Value("${app.jwt.refresh-token-duration-ms:604800000}")
    private long refreshTokenDurationMs;

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        // Delete any existing refresh token for this user first
        refreshTokenRepository.findByUser_Username(username)
            .ifPresent(token -> refreshTokenRepository.delete(token));

        refreshTokenRepository.flush(); // Force the delete to execute NOW

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public void deleteByUsername(String username) {
        userRepository.findByUsername(username)
                .ifPresent(refreshTokenRepository::deleteByUser);
    }
}