package com.example.javasepeti.map;

import com.example.javasepeti.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.model.Restaurant;
import org.modelmapper.PropertyMap;

public class RestaurantToRestaurantInfoDTO extends PropertyMap<Restaurant, RestaurantInfoDTO> {
    @Override
    protected void configure() {

    }
}
