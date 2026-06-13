package com.example.core.dto;

import com.example.core.enums.CourierStatus;

import lombok.Data;

@Data
public class UpdateCourierStatus {
    private CourierStatus status; // Must be one of AVAILABLE, BUSY, or OFFLINE

}
