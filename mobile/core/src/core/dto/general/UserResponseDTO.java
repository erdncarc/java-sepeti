package com.example.core.dto.general;

import com.example.core.enums.AccountStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseDTO extends UserDTO {

    private String name;

    private String email;

    private AccountStatus accountStatus;
}
