package com.example.javasepeti.repository;

import com.example.javasepeti.model.CusAddress;
import com.example.javasepeti.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CusAddressRepository extends AddressRepository<Customer,CusAddress>{

    Optional<CusAddress> findByAddressIdAndUser(Long id, Customer user);
}
