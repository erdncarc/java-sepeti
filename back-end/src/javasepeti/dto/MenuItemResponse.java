package com.example.javasepeti.dto;

import com.example.javasepeti.enums.Allergen;
import com.example.javasepeti.enums.Label;
import com.example.javasepeti.enums.MealCategory;
import com.example.javasepeti.model.MenuItem;
import com.example.javasepeti.model.Restaurant;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;


@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "itemId")
public class MenuItemResponse {

    protected Long itemId;
    private BigDecimal price;

    private BigDecimal rating;

    private String image;

    private List<Allergen> allergens;

    @PositiveOrZero
    private Integer quantity;



    protected String name;
    protected String description;


    protected List<MealCategory> categories;

    protected List<Label> labels;
    protected int protein;

    protected int carb;

    protected int fat;

    protected int calorie;


    public String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public MenuItemResponse(MenuItem item) {
        this.price = item.getPrice();
        this.image = item.getImage();
        this.allergens = item.getAllergens();
        this.quantity = item.getQuantity();
        this.itemId = item.getItemId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.categories = item.getCategories();
        this.labels = item.getLabels();
        this.protein = item.getProtein();
        this.carb = item.getCarb();
        this.fat = item.getFat();
        this.calorie = item.getCalorie();
        this.rating = item.getRating();
    }
}
