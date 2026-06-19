package com.insurance.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.dto.ApiResponse;
import com.insurance.dto.PasswordChangeRequest;
import com.insurance.entity.User;
import com.insurance.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<User>> getProfile(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok(profileService.getCurrentUser(authentication)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<User>> updateProfile(
            Authentication authentication, @RequestBody Map<String, String> body) {
        User user = profileService.updateProfile(authentication,
                body.get("fullName"), body.get("email"));
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", user));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication, @Valid @RequestBody PasswordChangeRequest request) {
        profileService.changePassword(authentication, request);
        return ResponseEntity.ok(ApiResponse.ok("Password changed successfully", null));
    }
}
