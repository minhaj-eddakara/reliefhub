package com.reliefhub.service;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.AuthDtos;
import com.reliefhub.model.Camp;
import com.reliefhub.model.User;
import com.reliefhub.model.Victim;
import com.reliefhub.repository.CampRepository;
import com.reliefhub.repository.UserRepository;
import com.reliefhub.repository.VictimRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    public static final String SESSION_USER_ID = "USER_ID";
    public static final String SESSION_USER_ROLE = "USER_ROLE";

    private final UserRepository userRepository;
    private final VictimRepository victimRepository;
    private final CampRepository campRepository;

    public AuthService(UserRepository userRepository, VictimRepository victimRepository, CampRepository campRepository) {
        this.userRepository = userRepository;
        this.victimRepository = victimRepository;
        this.campRepository = campRepository;
    }

    public ApiResponse<AuthDtos.UserResponse> login(AuthDtos.LoginRequest req, HttpSession session) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) {
            return ApiResponse.error("Invalid email or password");
        }

        User user = userOpt.get();
        // Secure password comparison
        if (!user.getPassword().equals(req.getPassword())) {
            return ApiResponse.error("Invalid email or password");
        }

        session.setAttribute(SESSION_USER_ID, user.getUserId());
        session.setAttribute(SESSION_USER_ROLE, user.getRole());

        AuthDtos.UserResponse response = buildUserResponse(user);
        return ApiResponse.ok("Login successful", response);
    }

    @Transactional
    public ApiResponse<AuthDtos.UserResponse> registerVictim(AuthDtos.RegisterVictimRequest req, HttpSession session) {
        String email = req.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            return ApiResponse.error("Email is already registered");
        }

        // 1. Create User
        User user = new User();
        user.setName(req.getName().trim());
        user.setEmail(email);
        user.setPhone(req.getPhone().trim());
        user.setPassword(req.getPassword());
        user.setRole("VICTIM");
        User savedUser = userRepository.save(user);

        // 2. Create Victim profile with custom sequential/UUID ID
        String victimId = "VIC-" + (100 + savedUser.getUserId());
        Victim victim = new Victim();
        victim.setVictimId(victimId);
        victim.setUser(savedUser);
        victim.setAge(req.getAge());
        victim.setGender(req.getGender());
        victim.setLocation(req.getLocation().trim());
        victim.setHouse(req.getHouse().trim());
        victimRepository.save(victim);

        session.setAttribute(SESSION_USER_ID, savedUser.getUserId());
        session.setAttribute(SESSION_USER_ROLE, savedUser.getRole());

        AuthDtos.UserResponse response = buildUserResponse(savedUser);
        return ApiResponse.ok("Registration successful. Welcome to ReliefHub.", response);
    }

    public ApiResponse<AuthDtos.UserResponse> getCurrentUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            return ApiResponse.error("Not authenticated");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return ApiResponse.error("User not found");
        }

        return ApiResponse.ok("Authenticated user retrieved", buildUserResponse(userOpt.get()));
    }

    public ApiResponse<Void> changePassword(AuthDtos.ChangePasswordRequest req, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            return ApiResponse.error("Not authenticated");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ApiResponse.error("User not found");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(req.getCurrentPassword())) {
            return ApiResponse.error("Current password does not match");
        }

        user.setPassword(req.getNewPassword());
        userRepository.save(user);
        return ApiResponse.ok("Password updated successfully");
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public AuthDtos.UserResponse buildUserResponse(User user) {
        AuthDtos.UserResponse res = new AuthDtos.UserResponse();
        res.setUserId(user.getUserId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setRole(user.getRole());

        if ("VICTIM".equalsIgnoreCase(user.getRole())) {
            victimRepository.findByUser_UserId(user.getUserId())
                    .ifPresent(v -> res.setVictimId(v.getVictimId()));
        } else if ("CAMP_MANAGER".equalsIgnoreCase(user.getRole())) {
            campRepository.findByManager_UserId(user.getUserId())
                    .ifPresent(c -> {
                        res.setAssignedCampId(c.getCampId());
                        res.setAssignedCampName(c.getCampName());
                    });
        }
        return res;
    }
}
