package com.example.javasepeti.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateCardRequest {

    private Long userId;
    private Long cardId;

    @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits.")
    private String cardNum;

    @Pattern(regexp = "^\\d{3}$", message = "CVC must be exactly 3 digits.")
    private String cvc;

    @NotNull
    private String cardHolder;

    @NotNull
    private String cardName;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Expiry date must be in MM/YY format.")
    private String expiryDate;
}
