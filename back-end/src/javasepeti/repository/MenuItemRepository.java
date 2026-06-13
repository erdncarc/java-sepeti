package com.example.javasepeti.repository;

import com.example.javasepeti.model.MenuItem;
import com.example.javasepeti.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends ItemRepository<MenuItem> {
    List<MenuItem> findByRestaurantUserId(Long restaurantId);

    Optional<MenuItem> findByItemIdAndRestaurant(Long itemId, Restaurant restaurant);

    Optional<MenuItem> findByHashIdAndParentIdNotNull(String hashId);

    List<MenuItem> findByRestaurantUserIdAndParentIdNull(Long restaurantId);
}