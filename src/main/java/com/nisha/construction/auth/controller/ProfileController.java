package com.nisha.construction.auth.controller;

import com.nisha.construction.auth.dto.ProfileResponse;
import com.nisha.construction.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal Object principal) {
        if (!(principal instanceof User)) {
            return ResponseEntity.status(401).build();
        }
        User user = (User) principal;
        return ResponseEntity.ok(ProfileResponse.builder()
                .email(user.getEmail())
                .name(user.getName() != null ? user.getName() : user.getUsername())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .provider(user.getProvider() != null ? user.getProvider().name() : "LOCAL")
                .build());
    }
}
