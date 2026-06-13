package com.example.javasepeti.service;

import com.example.javasepeti.dto.customer.AddCusAddressRequest;
import com.example.javasepeti.dto.UpdateCusAddressRequest;
import com.example.javasepeti.model.CusAddress;
import com.example.javasepeti.model.Customer;
import com.example.javasepeti.repository.AddressRepository;
import com.example.javasepeti.repository.CusAddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CusAddressService extends AddressService<Customer, CusAddress>{


    @Autowired
    public CusAddressService(AddressRepository<Customer, CusAddress> addressRepository, ModelMapper modelMapper) {
        super(addressRepository, modelMapper);
    }

    public CusAddress add(Customer customer, AddCusAddressRequest addCusAddressRequest){
        CusAddress cusAddress = modelMapper.map(addCusAddressRequest, CusAddress.class);
        cusAddress.setUser(customer);
        return addressRepository.save(cusAddress);
    }

    public CusAddress update(Customer customer, UpdateCusAddressRequest updateCusAddressRequest){
        CusAddress cusAddress =  findByAddressIdAndUser(updateCusAddressRequest.getAddressId(), customer);
        modelMapper.map(updateCusAddressRequest, cusAddress);
        return addressRepository.save(cusAddress);
    };

    public CusAddress findByAddressIdAndUser(Long addressId, Customer customer) {
        return ((CusAddressRepository) addressRepository).findByAddressIdAndUser(addressId,customer).orElseThrow(
                () -> new EntityNotFoundException("Address not found.")
        );
    }
}
