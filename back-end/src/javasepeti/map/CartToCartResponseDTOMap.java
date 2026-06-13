package com.example.javasepeti.map;

import com.example.javasepeti.dto.customer.CartResponseDTO;
import com.example.javasepeti.model.Cart;
import org.modelmapper.PropertyMap;

public class CartToCartResponseDTOMap extends PropertyMap<Cart, CartResponseDTO> {

    @Override
    protected void configure() {
        map().setCustomerId(source.getCustomer().getUserId());
        map().setTotalPrice(source.getTotalAmount());
    }
}
