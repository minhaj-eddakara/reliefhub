package com.reliefhub.service;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.AuthDtos;
import com.reliefhub.model.User;
import com.reliefhub.repository.CampRepository;
import com.reliefhub.repository.UserRepository;
import com.reliefhub.repository.VictimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final CampRepository campRepository;
    private final VictimRepository victimRepository;

    public UserService(UserRepository userRepository, AuthService authService, CampRepository campRepository, VictimRepository victimRepository) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.campRepository = campRepository;
        this.victimRepository = victimRepository;
    }

    public List<AuthDtos.UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(authService::buildUserResponse)
                .collect(Collectors.toList());
    }

    public List<AuthDtos.UserResponse> getUsersByRole(String role) {
        return userRepository.findByRole(role.toUpperCase()).stream()
                .map(authService::buildUserResponse)
                .collect(Collectors.toList());
    }

    public ApiResponse<AuthDtos.UserResponse> getUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(u -> ApiResponse.ok("User found", authService.buildUserResponse(u)))
                .orElse(ApiResponse.error("User not found"));
    }

    @Transactional
    public ApiResponse<AuthDtos.UserResponse> createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail().trim().toLowerCase())) {
            return ApiResponse.error("Email is already in use");
        }
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setName(user.getName().trim());
        user.setPhone(user.getPhone().trim());
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword("manager123");
        }
        User saved = userRepository.save(user);
        return ApiResponse.ok("User created successfully", authService.buildUserResponse(saved));
    }

    @Transactional
    public ApiResponse<AuthDtos.UserResponse> updateUser(Integer userId, User updated) {
        Optional<User> existingOpt = userRepository.findById(userId);
        if (existingOpt.isEmpty()) {
            return ApiResponse.error("User not found");
        }

        User existing = existingOpt.get();
        if (updated.getName() != null && !updated.getName().isBlank()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getPhone() != null && !updated.getPhone().isBlank()) {
            existing.setPhone(updated.getPhone().trim());
        }
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }
        if (updated.getRole() != null && !updated.getRole().isBlank()) {
            existing.setRole(updated.getRole().toUpperCase());
        }

        User saved = userRepository.save(existing);
        return ApiResponse.ok("User updated successfully", authService.buildUserResponse(saved));
    }

    @Transactional
    public ApiResponse<Void> deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            return ApiResponse.error("User not found");
        }
        // Dissociate from camps if camp manager
        campRepository.findByManager_UserId(userId).ifPresent(camp -> {
            camp.setManager(null);
            campRepository.save(camp);
        });

        // Dissociate from victims if victim
        victimRepository.findByUser_UserId(userId).ifPresent(victimRepository::delete);

        userRepository.deleteById(userId);
        return ApiResponse.ok("User deleted successfully");
    }
}
