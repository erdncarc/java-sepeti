package com.example.javasepeti.repository;

import com.example.javasepeti.model.Card;
import com.example.javasepeti.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
