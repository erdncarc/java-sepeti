package com.example.javasepeti.dto.auth;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequestDTO {
    @Email
    private String email;
    private String password;
} 