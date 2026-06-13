package com.example.javasepeti.repository;

import com.example.javasepeti.model.OpeningHour;
import com.example.javasepeti.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpeningHourRepository extends JpaRepository<OpeningHour, Long> {
    List<OpeningHour> findByRestaurant(Restaurant restaurant);
}

