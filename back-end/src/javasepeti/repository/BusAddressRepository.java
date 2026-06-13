package com.example.javasepeti.repository;

import com.example.javasepeti.model.BaseAddress;
import com.example.javasepeti.model.BusAddress;
import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusAddressRepository<R extends User,T extends BusAddress<R>> extends AddressRepository<R,T>{

    Optional<T> findByUser(User user);
}
