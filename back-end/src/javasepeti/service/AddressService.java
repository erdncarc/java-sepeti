package com.example.javasepeti.service;

import com.example.javasepeti.model.BaseAddress;
import com.example.javasepeti.model.CusAddress;
import com.example.javasepeti.model.User;
import com.example.javasepeti.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService<R extends User,T extends BaseAddress<R>> {

    protected final AddressRepository<R,T> addressRepository;

    protected final ModelMapper modelMapper;

    @Autowired
    public AddressService(AddressRepository<R, T> addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }


    public T findById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Address with id: %d is not exist", id))
        );
    }

    public T deleteById(Long id){
        T address = findById(id);
        addressRepository.deleteById(id);
        return address;
    }

    public T save(T address){
        return addressRepository.save(address);
    }





}
