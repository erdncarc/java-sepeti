package com.example.core.dto.customer;

import lombok.Data;


@Data
public class AddCartItemRequest {

    private Long menuItemId;

    private int quantity;
}