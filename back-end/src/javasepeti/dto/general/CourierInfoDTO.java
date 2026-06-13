package com.example.javasepeti.dto.general;


import com.example.javasepeti.enums.CourierStatus;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.Courier;
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
