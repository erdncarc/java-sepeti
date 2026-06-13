package com.example.javasepeti.repository;

import com.example.javasepeti.model.BaseAddress;
import com.example.javasepeti.model.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository<R extends User,T extends BaseAddress<R>> extends JpaRepository<T, Long> {

}
