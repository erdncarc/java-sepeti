package com.example.javasepeti.dto.customer;

import com.example.javasepeti.dto.general.AddressDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CusAddressResponseDTO extends AddressDTO {
    private String title;
    protected Long userId;

}
