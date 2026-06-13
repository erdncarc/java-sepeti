package com.example.core.dto.customer;

import com.example.core.dto.general.CartItemDTO;
import com.example.core.dto.general.RestaurantInfoDTO;
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
