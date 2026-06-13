package com.example.javasepeti.dto;

import com.example.javasepeti.dto.general.BusAddressDTO;
import com.example.javasepeti.dto.general.UserDTO;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.Restaurant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderRestaurantDTO extends UserDTO {

    private String restaurantName;

    private BusAddressDTO busAddress;

    private String description;

    private Double distance;
    private boolean isOpen;

}
