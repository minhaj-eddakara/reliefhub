package com.reliefhub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @NotBlank(message = "Name is required")
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 50)
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20)
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @NotBlank(message = "Role is required")
    @Size(max = 50)
    @Column(name = "role", length = 50, nullable = false)
    private String role; // 'ADMIN', 'CAMP_MANAGER', 'VICTIM'

    public User() {
    }

    public User(Integer userId, String name, String email, String phone, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
