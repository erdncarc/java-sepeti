package com.example.javasepeti.model;

import com.example.javasepeti.dto.AddMenuItemRequest;
import com.example.javasepeti.dto.MenuItemResponse;
import com.example.javasepeti.dto.UpdateMenuItemRequest;
import com.example.javasepeti.util.HashUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.javasepeti.enums.Allergen;
import com.example.javasepeti.enums.MealCategory;
import com.example.javasepeti.enums.Label;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@PrimaryKeyJoinColumn(name = "itemId")
@Table(name = "menu_items")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuItem extends Item {

    @Column(nullable = false, unique = false)
    private String hashId;

    @Column(nullable = true)
    private Long parentId = null;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;


    @ElementCollection(targetClass = Allergen.class)
    @CollectionTable(
            name = "menu_item_allergens",
            joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "itemId")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "allergen")
    private List<Allergen> allergens = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "res_id", nullable = false, updatable = false)
    @JsonIgnore
    @NotNull
    private Restaurant restaurant;

    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must not exceed 5.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.")
    @Digits(integer = 1, fraction = 1, message = "Rating must have only 1 decimal place.")
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;



    public String createHash() {
        String composed = getName() + "|" + this.getDescription() + "|" + getPrice() + "|" + getAllergens().stream().map(Enum::name).collect(Collectors.joining("|")) +
                "|" + getImage() + "|" + getRestaurant().getUserId();
        return HashUtils.sha256(composed);
    };

}