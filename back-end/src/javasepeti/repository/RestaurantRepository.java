package com.example.javasepeti.repository;

import org.springframework.stereotype.Repository;

import com.example.javasepeti.model.Restaurant;

@Repository
public interface RestaurantRepository extends UserRepository<Restaurant> {
} 