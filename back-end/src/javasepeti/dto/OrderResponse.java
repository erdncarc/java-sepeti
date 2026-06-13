package com.example.javasepeti.dto;

import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.enums.PaymentMethod;
import com.example.javasepeti.model.CusAddress;
import com.example.javasepeti.model.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private Long orderId;
    private LocalDateTime date;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private String note;

    private Long cartId;
    private Long customerId;
    private String customerName;

    // Address info from CusAddress
    private BigDecimal latitude;
    private BigDecimal longitude;

}
