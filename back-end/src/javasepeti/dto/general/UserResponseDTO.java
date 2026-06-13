package com.example.javasepeti.dto.general;

import com.example.javasepeti.enums.AccountStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseDTO extends UserDTO {

    private String name;

    @NotBlank
    @Email(message = "Please provide a valid email address.")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email format is invalid."
    )
    private String email;

    private AccountStatus accountStatus;
}
