package com.example.javasepeti.dto.general;

import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.UserRole;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {

    private Long userId;

    private String phone;

    private String image;


    private UserRole role;
}
