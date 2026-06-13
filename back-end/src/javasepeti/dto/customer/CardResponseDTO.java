package com.example.javasepeti.dto.customer;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CardResponseDTO {

    private Long cardId;

    private String cardNum;

    private String cardHolder;

    private String cardName;

    private String expiryDate;

    private Long customerId;
}
