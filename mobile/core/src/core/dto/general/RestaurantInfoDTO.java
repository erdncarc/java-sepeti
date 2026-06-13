package com.example.core.dto.general;

import com.example.core.dto.restaurant.OpeningHourDTO;
import com.example.core.model.Rating;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantInfoDTO extends UserDTO {

    private String restaurantName;

    private Rating rating;

    private List<MenuItemDTO> menuItems;

    private BusAddressDTO busAddress;

    private String description;

    private List<OpeningHourDTO> openingHours;

    private Double distance;
    private boolean isOpen;

}
