package com.example.javasepeti.dto.courier;

import com.example.javasepeti.model.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSummaryDTO {
    private String name;
    private String image;
    private Rating rating;
}
