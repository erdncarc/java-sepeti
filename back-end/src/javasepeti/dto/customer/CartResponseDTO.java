package com.example.javasepeti.dto.customer;

import com.example.javasepeti.dto.general.CartItemDTO;
import com.example.javasepeti.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.model.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponseDTO {


    private Long cartId;

    private Long customerId;

    private List<CartItemDTO> cartItems;


    private RestaurantInfoDTO restaurant;

    private BigDecimal totalPrice;

}
