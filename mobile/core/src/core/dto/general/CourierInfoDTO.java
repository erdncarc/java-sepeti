package com.example.core.dto.general;


import com.example.core.enums.CourierStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourierInfoDTO extends UserDTO {

    private BigDecimal rating;
    private CourierStatus status;

    private BusAddressDTO address;
}
