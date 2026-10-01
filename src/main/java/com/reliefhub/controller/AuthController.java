package com.reliefhub.controller;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.AuthDtos;
import com.reliefhub.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDtos.UserResponse>> login(
            @Valid @RequestBody AuthDtos.LoginRequest req,
            HttpSession session) {
        ApiResponse<AuthDtos.UserResponse> resp = authService.login(req, session);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthDtos.UserResponse>> register(
            @Valid @RequestBody AuthDtos.RegisterVictimRequest req,
            HttpSession session) {
        ApiResponse<AuthDtos.UserResponse> resp = authService.registerVictim(req, session);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthDtos.UserResponse>> getCurrentUser(HttpSession session) {
        ApiResponse<AuthDtos.UserResponse> resp = authService.getCurrentUser(session);
        if (!resp.isSuccess()) {
            return ResponseEntity.status(401).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody AuthDtos.ChangePasswordRequest req,
            HttpSession session) {
        ApiResponse<Void> resp = authService.changePassword(req, session);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully"));
    }
}
