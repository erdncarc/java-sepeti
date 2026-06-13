package com.example.javasepeti.repository;

import com.example.javasepeti.model.Customer;
import com.example.javasepeti.model.Order;
import com.example.javasepeti.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);

    List<Order> findByRestaurant(Restaurant restaurant);


}
