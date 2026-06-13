package com.example.core.dto.customer;

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
