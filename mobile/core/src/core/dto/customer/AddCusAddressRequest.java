package com.example.core.dto.customer;


import com.example.core.model.Location;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddCusAddressRequest {
    private String title;

    private String street;

    private String city;

    private String town;

    private String apartment;

    private Integer number;

    private String details;

    private Location location;


}
