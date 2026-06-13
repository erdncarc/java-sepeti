package com.example.javasepeti.repository;

import com.example.javasepeti.model.DailyNutrition;
import com.example.javasepeti.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyNutritionRepository extends JpaRepository<DailyNutrition, Long> {
    Optional<DailyNutrition> findByCustomerUserIdAndDate(Long userId, LocalDate date);
    boolean existsByItemsContaining(Item item);

}
