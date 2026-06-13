package com.example.javasepeti.dto;

import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.model.CusAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {
    private Long orderId;
    private String restaurantName;
    private String customerName;
    private CusAddress address;
    private LocalDateTime date;
    private OrderStatus status;
}