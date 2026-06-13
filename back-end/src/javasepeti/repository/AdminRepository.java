package com.example.javasepeti.repository;

import org.springframework.stereotype.Repository;

import com.example.javasepeti.model.Admin;

@Repository
public interface AdminRepository extends UserRepository<Admin> {
} 