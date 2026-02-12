package com.nisha.construction.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.nisha.construction.auth.dto.AuthRequest;
import com.nisha.construction.auth.dto.AuthResponse;
import com.nisha.construction.security.entity.AuthProvider;
import com.nisha.construction.security.entity.Role;
import com.nisha.construction.security.entity.User;
import com.nisha.construction.security.jwt.JwtUtil;
import com.nisha.construction.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @org.springframework.beans.factory.annotation.Value("${ADMIN_USERNAME:admin}")
    private String adminUsername;

    @org.springframework.beans.factory.annotation.Value("${ADMIN_EMAIL:admin@buildpulse.com}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${ADMIN_PASSWORD:admin123}")
    private String adminPassword;

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());

        String jwtToken = jwtUtil.generateToken(claims, user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse register(com.nisha.construction.auth.dto.RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already in use");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) // Default role
                .provider(AuthProvider.LOCAL)
                .build();

        user = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());

        String jwtToken = jwtUtil.generateToken(claims, user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }

    // Helper to generic initial admin user (call manually or on startup)
    public void registerAdmin() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            var user = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .provider(AuthProvider.LOCAL)
                    .build();
            userRepository.save(user);
        }
    }
}
