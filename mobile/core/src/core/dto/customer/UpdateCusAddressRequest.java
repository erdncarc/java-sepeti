package com.example.core.dto.customer;

import com.example.core.model.Location;

import lombok.Data;

@Data
public class UpdateCusAddressRequest {

    private Long addressId;
    private String title;

    private String street;

    private String city;

    private String town;

    private String apartment;

    private Integer number;

    private Location location;

    private String details;
}
