package com.example.javasepeti.map;

import com.example.javasepeti.dto.general.CartItemDTO;
import com.example.javasepeti.model.CartItem;
import org.modelmapper.PropertyMap;

public class CartItemToCartItemDTO extends PropertyMap<CartItem, CartItemDTO> {
    @Override
    protected void configure() {
        map().setItemId(source.getMenuItem().getItemId());
        map().setName(source.getMenuItem().getName());
        map().setDescription(source.getMenuItem().getDescription());
        map().setCategories(source.getMenuItem().getCategories());
        map().setLabels(source.getMenuItem().getLabels());
        map().setProtein(source.getMenuItem().getProtein());
        map().setCarb(source.getMenuItem().getCarb());
        map().setFat(source.getMenuItem().getFat());
        map().setCalorie(source.getMenuItem().getCalorie());
    }
}