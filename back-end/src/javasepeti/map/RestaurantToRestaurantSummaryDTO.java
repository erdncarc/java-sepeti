package com.example.javasepeti.map;

import com.example.javasepeti.dto.courier.RestaurantSummaryDTO;
import com.example.javasepeti.model.Restaurant;
import org.modelmapper.PropertyMap;

public class RestaurantToRestaurantSummaryDTO extends PropertyMap<Restaurant, RestaurantSummaryDTO> {

    @Override
    protected void configure() {
        map().setName(source.getRestaurantName());
        map().setImage(source.getImage());
        map().setRating(source.getRating() != null ? source.getRating() : null);
    }
}
