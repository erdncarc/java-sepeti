package com.example.core.dto;

import lombok.Data;

@Data
public class CourierUpdateProfile {
    private String name;
    private String email;

    private String phoneNumber;
    private String password;
}
