package com.example.javasepeti.repository;

import com.example.javasepeti.model.Card;
import com.example.javasepeti.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardId(Long id);

    Optional<Card> findByCardIdAndCustomer(Long cardId, Customer customer);

}
