package com.example.javasepeti.repository;

import com.example.javasepeti.enums.CourierStatus;
import com.example.javasepeti.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends UserRepository<Courier> {
    List<Courier> findByStatus(CourierStatus status);
} 