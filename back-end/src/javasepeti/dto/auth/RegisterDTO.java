package com.example.javasepeti.dto.auth;

import com.example.javasepeti.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank
    private String name;
    @NotBlank
    @Email(message = "Please provide a valid email address.")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email format is invalid."
    )
    private String email;
    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    private UserRole role;
} 