package com.example.core.dto;


import com.example.core.model.Location;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BusAddressRequest {

    protected String street;

    protected String city;

    protected String town;

    protected String apartment;

    protected Integer number;

    protected String details;

    protected Location location;
    private int rangeValue;
}
