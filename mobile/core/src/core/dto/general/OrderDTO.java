package com.example.core.dto.general;

import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.enums.OrderAssignmentStatus;
import com.example.core.enums.OrderStatus;
import com.example.core.enums.PaymentMethod;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {

    private Long orderId;

    private LocalDateTime date;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private PaymentMethod paymentMethod;

    private String note;

    private BigDecimal rating;

    private CusAddressResponseDTO address;

    private CartResponseDTO cart;

    private RestaurantInfoDTO restaurant;

    private CourierInfoDTO courier;

    private CardResponseDTO card;

    private CustomerDTO customer;

    private ReviewDTO review;

    private RefundDTO refund;

    private OrderAssignmentStatus assignmentStatus;
}
