package com.example.core.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface IEntityService<T,K> {

    @GET(".")
    Call<List<T>> getAll();

    @GET("{id}")
    Call<T> get(@Path("id") K id);

    @POST(".")
    Call<Void> create(@Body T entity);

    @PUT("{id}")
    Call<Void> update(@Path("id") K id, @Body T entity);

    @DELETE("{id}")
    Call<Void> delete(@Path("id") K id);
}
