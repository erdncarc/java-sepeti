package com.example.javasepeti.dto;

import com.example.javasepeti.enums.CourierStatus;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.Courier;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCourierStatus {
    @NotNull
    private CourierStatus status; // Must be one of AVAILABLE, BUSY, or OFFLINE

}
