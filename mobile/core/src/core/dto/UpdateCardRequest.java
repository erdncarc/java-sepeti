package com.example.core.dto;


import lombok.Data;

@Data
public class UpdateCardRequest {

    private Long userId;
    private Long cardId;

    private String cardNum;

    private String cvc;

    private String cardHolder;

    private String cardName;

    private String expiryDate;
}
