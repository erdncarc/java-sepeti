package com.example.core.dto.customer;

import com.example.core.dto.general.AddressDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CusAddressResponseDTO extends AddressDTO {
    private String title;
    protected Long userId;

}
