package com.example.javasepeti.dto;

import com.example.javasepeti.Utils;
import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.enums.UserRole;
import com.example.javasepeti.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRestaurantResponse {

    private Long userId;

    private String name;

    @Pattern(regexp = "^5\\d{9}$", message = "Phone number must start with 5 and contain exactly 10 digits.")
    private String phone;

    private String image;
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must not exceed 5.")
    @Digits(integer = 1, fraction = 1, message = "Rating must have only 1 decimal place.")
    private BigDecimal rating;

    private List<MenuItemResponse> menuItems;

    private BusAddress<Restaurant> busAddress;

    private String description;

    private List<OpeningHour> openingHours;

    private Double distance;  // Distance from customer (in km)
    private boolean isOpen;   // Whether the restaurant is open now


}