package com.example.core.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rating {

    private BigDecimal service;

    private BigDecimal taste;

    private BigDecimal delivery ;

    public BigDecimal total() {
        if(taste == null || service == null || delivery == null){
            return BigDecimal.ZERO;
        }
        return  BigDecimal.valueOf(Math.round(getService().add(getTaste()).add(getDelivery()).doubleValue() * 10) / 10.0);
    }
}
