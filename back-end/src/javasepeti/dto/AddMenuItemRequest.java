package com.example.javasepeti.dto;

import com.example.javasepeti.enums.Allergen;
import com.example.javasepeti.enums.Label;
import com.example.javasepeti.enums.MealCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddMenuItemRequest {

    @NotBlank(message = "Menu item name is required.")
    private String name;

    @Positive(message = "Price must be a positive value.")
    private BigDecimal price;

    private String image; // Optional, if you want to allow image upload

    private List<Allergen> allergens; // Optional allergens for the menu item

    @Positive(message = "Quantity must be a positive value.")
    private Integer quantity = 0;

    private String description;


    private List<MealCategory> categories;


    private List<Label> labels;

    @NotNull
    private int protein;

    @NotNull
    private int carb;

    @NotNull
    private int fat;

    @NotNull
    private int calorie;
}