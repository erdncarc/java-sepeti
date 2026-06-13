package com.example.javasepeti.repository;

import com.example.javasepeti.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    Optional<T> findByName(String username);
    Optional<T> findByEmail(String email);

    boolean existsByName(String username);
    boolean existsByEmail(String email);
} 