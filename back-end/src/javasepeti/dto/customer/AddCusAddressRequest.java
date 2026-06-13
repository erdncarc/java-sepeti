package com.example.javasepeti.dto.customer;

import com.example.javasepeti.model.Location;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddCusAddressRequest {
    private String title;

    private String street;

    private String city;

    private String town;

    private String apartment;

    @Column(name = "number", nullable = false)
    @Min(value = 0, message = "Number must be a non-negative integer.")
    private Integer number;

    private String details;

    private Location location;


}
