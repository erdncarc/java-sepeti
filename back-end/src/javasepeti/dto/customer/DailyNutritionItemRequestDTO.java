package com.example.javasepeti.dto.customer;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyNutritionItemRequestDTO {
    private LocalDate date;
    private String name;
    private int protein;
    private int carb;
    private int fat;
    private int calorie;
    private Integer quantity;
}
