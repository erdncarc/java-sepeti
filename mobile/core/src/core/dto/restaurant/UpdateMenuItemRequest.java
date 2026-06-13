package com.example.core.dto.restaurant;


import com.example.core.enums.Allergen;
import com.example.core.enums.Label;
import com.example.core.enums.MealCategory;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateMenuItemRequest {

    private Long itemId;
    private String name;

    private BigDecimal price;

    private String image; // Optional, if you want to allow image upload

    private List<Allergen> allergens; // Optional allergens for the menu item

    private Integer quantity = 0;

    private String description;

    private List<MealCategory> categories;

    private List<Label> labels;

    private int protein;

    private int carb;

    private int fat;

    private int calorie;
}
