package com.example.javasepeti.dto.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DailyNutritionItemDTO {

    Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String image;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    private double calorie;
    private double protein;
    private double carb;
    private double fat;

    public DailyNutritionItemDTO(Long id, String name, String image, int quantity, double calorie, double protein, double carb, double fat) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.calorie = calorie;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
    }
}
