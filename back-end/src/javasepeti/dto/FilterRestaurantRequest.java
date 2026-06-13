package com.example.javasepeti.dto;

import com.example.javasepeti.enums.RestaurantSortOption;
import com.example.javasepeti.enums.SortDirection;
import com.example.javasepeti.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class FilterRestaurantRequest {
    private RestaurantSortOption sortBy;
    private SortDirection sortDirection;

    private boolean isOpen;

    private boolean isClosed;
    Location location;
}