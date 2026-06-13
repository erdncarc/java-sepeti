package com.example.javasepeti.dto.customer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReviewRequestDTO {
    private Long customerId;
    private Long restaurantId;
    private Long orderId;
    @NotNull(message = "Menu item ID is required")
    private Long menuItemId;
    private BigDecimal taste;
    private BigDecimal delivery;
    private String comment;
}
