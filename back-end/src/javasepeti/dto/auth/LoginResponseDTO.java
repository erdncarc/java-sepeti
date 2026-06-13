package com.example.javasepeti.dto.auth;

import com.example.javasepeti.enums.UserRole;
import lombok.Data;

@Data
public class LoginResponseDTO {

    String token;
    String username;
    UserRole role;
}
