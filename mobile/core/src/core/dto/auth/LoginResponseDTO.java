package com.example.core.dto.auth;

import com.example.core.enums.UserRole;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponseDTO {
    String token;
    String username;
    UserRole role;
}
