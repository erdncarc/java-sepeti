package com.example.core.dto;

import com.example.core.dto.general.BusAddressDTO;
import com.example.core.dto.general.UserDTO;
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
