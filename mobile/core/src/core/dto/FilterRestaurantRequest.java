package com.example.core.dto;


import com.example.core.enums.RestaurantSortOption;
import com.example.core.enums.SortDirection;
import com.example.core.model.Location;

import lombok.Data;


@Data
public class FilterRestaurantRequest {
    private RestaurantSortOption sortBy;
    private SortDirection sortDirection;
    private boolean open;
    private boolean closed;
    Location location;
}