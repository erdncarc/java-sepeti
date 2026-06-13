package com.example.javasepeti.dto;

import com.example.javasepeti.enums.Allergen;
import com.example.javasepeti.enums.MenuItemSortOption;
import com.example.javasepeti.enums.SortDirection;
import lombok.Data;

import java.util.List;

@Data
public class FilterMenuItemRequest {
    private MenuItemSortOption sortBy = MenuItemSortOption.PRICE;
    private SortDirection sortDirection = SortDirection.ASC;

    private int minCalories = 0;
    private int maxCalories = 1000;

    private int minProtein = 0;
    private int maxProtein = 50;

    private int minFats = 0;
    private int maxFats = 50;

    private int minCarbs = 0;
    private int maxCarbs = 100;

    private boolean isVegan = false;
    private boolean isVegetarian = false;

    private List<Allergen> allergens;
}
