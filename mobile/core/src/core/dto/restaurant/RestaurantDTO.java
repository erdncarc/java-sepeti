package com.example.core.dto.restaurant;

import com.example.core.dto.general.BusAddressDTO;
import com.example.core.dto.general.UserResponseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantDTO extends UserResponseDTO {

    private String restaurantName;

    private BigDecimal rating;

    private Integer feeRatio;

    private Integer lpRatio;

    private BusAddressDTO busAddress;

    private String description;

    private List<OpeningHourDTO> openingHours;
}
