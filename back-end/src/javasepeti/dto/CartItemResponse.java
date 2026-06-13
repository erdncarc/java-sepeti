package com.example.javasepeti.dto;

import com.example.javasepeti.model.Cart;
import com.example.javasepeti.model.CartItem;
import com.example.javasepeti.model.MenuItem;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cartItemId")
public class CartItemResponse {

    private Long cartItemId;

    private Integer quantity;

    private MenuItemResponse menuItem;


    public CartItemResponse(CartItem item) {
        cartItemId = item.getCartItemId();
        quantity = item.getQuantity();
        menuItem = new MenuItemResponse(item.getMenuItem());
    }




}
