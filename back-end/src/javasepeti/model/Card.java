package com.example.javasepeti.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;


    @Column(name = "card_num", nullable = false, unique = false, length = 16)
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits.")
    private String cardNum;

    @Column(name = "cvc", nullable = false)
    @Pattern(regexp = "^\\d{3}$", message = "CVC must be exactly 3 digits.")
    private String cvc;

    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "exp_date", nullable = false, length = 5)
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Expiry date must be in MM/YY format.")
    private String expiryDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

}