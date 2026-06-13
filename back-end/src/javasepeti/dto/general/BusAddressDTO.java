package com.example.javasepeti.dto.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusAddressDTO extends AddressDTO{

    private Integer rangeValue;

    private Long userId;

}
