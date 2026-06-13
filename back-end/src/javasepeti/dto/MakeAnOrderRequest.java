package com.example.javasepeti.dto;

import com.example.javasepeti.enums.PaymentMethod;
import com.example.javasepeti.model.Card;
import com.example.javasepeti.model.CusAddress;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MakeAnOrderRequest {
    private Long cardId; // optional if CASH

    @NotNull(message = "Address ID must be provided.")
    private Long addressId;

    @NotNull(message = "Payment method must be specified.")
    private PaymentMethod paymentMethod;

    private String note; // optional delivery instructions

}
