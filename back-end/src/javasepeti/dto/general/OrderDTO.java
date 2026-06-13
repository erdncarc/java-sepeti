package com.example.javasepeti.dto.general;

import com.example.javasepeti.dto.customer.*;
import com.example.javasepeti.dto.general.CourierInfoDTO;
import com.example.javasepeti.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.enums.OrderAssignmentStatus;
import com.example.javasepeti.enums.OrderStatus;
import com.example.javasepeti.enums.PaymentMethod;
import com.example.javasepeti.model.*;
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
