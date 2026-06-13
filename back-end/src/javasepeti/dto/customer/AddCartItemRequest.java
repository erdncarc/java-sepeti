package com.example.javasepeti.dto.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddCartItemRequest {

    @NotNull
    private Long menuItemId;

    @Positive
    private int quantity;
}