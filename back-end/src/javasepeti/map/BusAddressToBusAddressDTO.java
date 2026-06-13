package com.example.javasepeti.map;

import com.example.javasepeti.dto.general.BusAddressDTO;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.User;
import org.modelmapper.PropertyMap;

public class BusAddressToBusAddressDTO<T extends User> extends PropertyMap<BusAddress<T>, BusAddressDTO> {
    @Override
    protected void configure() {
        map().setUserId(source.getUser().getUserId());
    }
}
