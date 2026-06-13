package com.example.javasepeti.dto.general;

import com.example.javasepeti.model.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDTO {
    protected Long addressId;

    protected String street;
    protected String city;

    protected String town;

    protected String apartment;

    @Min(value = 0, message = "Number must be a non-negative integer.")
    protected Integer number;

    protected String details;

    @NotNull
    protected Location location;


}
