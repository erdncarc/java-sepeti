package com.example.javasepeti.dto;

import com.example.javasepeti.model.Location;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BusAddressRequest {

    protected String street;

    protected String city;

    protected String town;

    protected String apartment;

    @Min(value = 0, message = "Number must be a non-negative integer.")
    protected Integer number;

    protected String details;

    protected Location location;
    @Min(value = 1, message = "Range must be a positive number.")
    private int rangeValue;
}
