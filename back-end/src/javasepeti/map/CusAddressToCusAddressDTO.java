package com.example.javasepeti.map;

import com.example.javasepeti.dto.customer.CusAddressResponseDTO;
import com.example.javasepeti.model.CusAddress;
import org.modelmapper.PropertyMap;

public class CusAddressToCusAddressDTO extends PropertyMap<CusAddress, CusAddressResponseDTO> {
    @Override
    protected void configure() {
        map().setUserId(source.getUser().getUserId());
    }
}
