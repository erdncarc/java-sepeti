package com.example.javasepeti.repository;

import com.example.javasepeti.model.Courier;
import com.example.javasepeti.model.MenuItem;
import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMenuItem(MenuItem menuItem);
    List<Review> findByOrder_Courier_UserId(Long courierId);

    List<Review> findByRestaurant_UserId(Long restaurantId);

    List<Review> findByOrder_Courier(Courier courier);
    List<Review> findByMenuItem_Restaurant(Restaurant restaurant);
    List<Review> findByMenuItem_Restaurant_UserId(Long restaurantId);

}
