package com.example.javasepeti.controller;

import com.example.javasepeti.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    RestaurantService restaurantService;
    ModelMapper modelMapper;

    public PublicController(RestaurantService restaurantService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantInfoDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAll();
        return ResponseEntity.ok(restaurants.stream().map(r -> modelMapper.map(r, RestaurantInfoDTO.class)).collect(Collectors.toList()));
    }
}