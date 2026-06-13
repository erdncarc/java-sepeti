package com.example.javasepeti.dto.restaurant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateRestaurantInfoDTO {

    private String description;

    private String name;

    private String restaurantName;

    @NotBlank
    @Email(message = "Please provide a valid email address.")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email format is invalid."
    )
    private String email;

    @Pattern(regexp = "^5\\d{9}$", message = "Phone number must start with 5 and contain exactly 10 digits.")
    private String phone;


}
