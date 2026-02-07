package com.example.classroom_management.controller;

import com.example.classroom_management.dto.AuthRequest;
import com.example.classroom_management.dto.AuthResponse;
import com.example.classroom_management.dto.RefreshTokenRequest;
import com.example.classroom_management.model.RefreshToken;
import com.example.classroom_management.service.CustomUserDetailsService;
import com.example.classroom_management.service.JwtService;
import com.example.classroom_management.service.RefreshTokenService;
import com.example.classroom_management.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            var userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(userDetails);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

            return new AuthResponse(accessToken, refreshToken.getToken());
        } else {
            throw new UsernameNotFoundException("Invalid user request !");
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            String username = jwtService.extractUsername(token);
            refreshTokenService.deleteByUsername(username);

            return "Logout successful";
        }
        return "No Token provided";
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    var userDetails = userDetailsService.loadUserByUsername(user.getUsername());

                    String accessToken = jwtService.generateToken(userDetails);
                    return new AuthResponse(accessToken, request.getToken());
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}