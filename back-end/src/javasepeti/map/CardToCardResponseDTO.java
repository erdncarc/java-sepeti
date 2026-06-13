package com.example.javasepeti.map;

import com.example.javasepeti.dto.customer.CardResponseDTO;
import com.example.javasepeti.model.Card;
import org.modelmapper.PropertyMap;

public class CardToCardResponseDTO extends PropertyMap<Card, CardResponseDTO> {
    @Override
    protected void configure() {
        map().setCustomerId(source.getCustomer().getUserId());
    }
}
