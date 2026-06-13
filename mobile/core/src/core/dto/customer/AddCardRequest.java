package com.example.core.dto.customer;

import lombok.Data;

@Data
public class AddCardRequest {

    private String cardNum;

    private String cvc;

    private String cardHolder;

    private String cardName;

    private String expiryDate;

}
