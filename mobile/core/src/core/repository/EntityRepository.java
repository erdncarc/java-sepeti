package com.example.core.repository;

import com.example.core.network.ApiClient;

import retrofit2.Retrofit;

public abstract class EntityRepository {

    protected final Retrofit retrofit;

    public EntityRepository(String endpoint) {
        this.retrofit = ApiClient.getClient(endpoint);
    }
}
