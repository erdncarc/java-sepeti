package com.example.javasepeti.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Embeddable
public class Rating {

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must not exceed 5.")
    @Digits(integer = 1, fraction = 1, message = "Rating must have only 1 decimal place.")
    private BigDecimal taste = new BigDecimal(0);

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must not exceed 5.")
    @Digits(integer = 1, fraction = 1, message = "Rating must have only 1 decimal place.")
    private BigDecimal delivery = new BigDecimal(0);


    public BigDecimal total() {
        double avg = (taste.doubleValue() + delivery.doubleValue()) / 2.0;
        double rounded = Math.round(avg * 10.0) / 10.0;
        return BigDecimal.valueOf(rounded);
    }

}
