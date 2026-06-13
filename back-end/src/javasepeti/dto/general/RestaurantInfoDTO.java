package com.example.javasepeti.dto.general;

import com.example.javasepeti.dto.general.MenuItemDTO;
import com.example.javasepeti.dto.general.UserDTO;
import com.example.javasepeti.dto.restaurant.OpeningHourDTO;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.OpeningHour;
import com.example.javasepeti.model.Rating;
import com.example.javasepeti.model.Restaurant;
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
