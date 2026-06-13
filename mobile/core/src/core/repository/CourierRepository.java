package com.example.core.repository;

import com.example.core.network.BaseCallback;
import com.example.core.service.ICourierService;

import lombok.Getter;

public class CourierRepository extends EntityRepository{

    @Getter
    private static final CourierRepository instance = new CourierRepository("courier");

    private final ICourierService service;
    public CourierRepository(String endpoint) {
        super(endpoint);
        this.service =  this.retrofit.create(ICourierService.class);
    }

}
