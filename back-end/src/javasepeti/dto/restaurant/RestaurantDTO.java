package com.example.javasepeti.dto.restaurant;

import com.example.javasepeti.dto.general.UserResponseDTO;
import com.example.javasepeti.model.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantDTO extends UserResponseDTO {

    private String restaurantName;

    private Rating rating;

    private Integer feeRatio;

    private Integer lpRatio;

    private BusAddress<Restaurant> busAddress;

    private String description;

    private List<OpeningHour> openingHours;
}
