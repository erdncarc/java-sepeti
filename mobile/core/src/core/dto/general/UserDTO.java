package com.example.core.dto.general;


import com.example.core.enums.UserRole;

import lombok.Data;

@Data
public class UserDTO {

    private Long userId;

    private String phone;

    private String image;


    private UserRole role;
}
