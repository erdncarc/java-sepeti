package com.example.core.dto;

import com.example.core.enums.PaymentMethod;

import lombok.Data;


@Data
public class MakeAnOrderRequest {
    private Long cardId; // optional if CASH

    private Long addressId;

    private PaymentMethod paymentMethod;

    private String note; // optional delivery instructions

}
