package com.reliefhub.controller;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.AuthDtos;
import com.reliefhub.model.User;
import com.reliefhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthDtos.UserResponse>>> getUsers(
            @RequestParam(required = false) String role) {
        List<AuthDtos.UserResponse> users;
        if (role != null && !role.isBlank()) {
            users = userService.getUsersByRole(role);
        } else {
            users = userService.getAllUsers();
        }
        return ResponseEntity.ok(ApiResponse.ok("Users retrieved", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthDtos.UserResponse>> getUserById(@PathVariable Integer id) {
        ApiResponse<AuthDtos.UserResponse> resp = userService.getUserById(id);
        if (!resp.isSuccess()) {
            return ResponseEntity.status(404).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthDtos.UserResponse>> createUser(@Valid @RequestBody User user) {
        ApiResponse<AuthDtos.UserResponse> resp = userService.createUser(user);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthDtos.UserResponse>> updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {
        ApiResponse<AuthDtos.UserResponse> resp = userService.updateUser(id, user);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        ApiResponse<Void> resp = userService.deleteUser(id);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }
}
