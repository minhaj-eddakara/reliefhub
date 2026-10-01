package com.reliefhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterVictimRequest {
        @NotBlank(message = "Name is required")
        @Size(max = 50)
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 50)
        private String email;

        @NotBlank(message = "Phone number is required")
        @Size(max = 20)
        private String phone;

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
        private String password;

        @NotNull(message = "Age is required")
        private Integer age;

        @NotBlank(message = "Gender is required")
        private String gender;

        @NotBlank(message = "Location is required")
        @Size(max = 100)
        private String location;

        @NotBlank(message = "House/Address details are required")
        @Size(max = 100)
        private String house;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getHouse() { return house; }
        public void setHouse(String house) { this.house = house; }
    }

    public static class ChangePasswordRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class UserResponse {
        private Integer userId;
        private String name;
        private String email;
        private String phone;
        private String role;
        private String victimId;
        private String assignedCampId;
        private String assignedCampName;

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getVictimId() { return victimId; }
        public void setVictimId(String victimId) { this.victimId = victimId; }

        public String getAssignedCampId() { return assignedCampId; }
        public void setAssignedCampId(String assignedCampId) { this.assignedCampId = assignedCampId; }

        public String getAssignedCampName() { return assignedCampName; }
        public void setAssignedCampName(String assignedCampName) { this.assignedCampName = assignedCampName; }
    }
}
