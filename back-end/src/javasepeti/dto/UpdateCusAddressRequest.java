package com.example.javasepeti.dto;

import com.example.javasepeti.model.Location;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCusAddressRequest {

    private Long addressId;
    private String title;

    private String street;

    private String city;

    private String town;

    private String apartment;

    @Column(name = "number", nullable = false)
    @Min(value = 0, message = "Number must be a non-negative integer.")
    private Integer number;

    private Location location;

    private String details;
}
