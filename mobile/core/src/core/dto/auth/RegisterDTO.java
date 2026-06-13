package com.example.core.dto.auth;

import com.example.core.enums.UserRole;

import lombok.Data;

@Data
public class RegisterDTO {
    private String name;
    private String email;
    private String password;

    private UserRole role;
} 