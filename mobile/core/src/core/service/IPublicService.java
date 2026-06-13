package com.example.core.service;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.dto.restaurant.RestaurantDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPublicService {


    @GET("restaurants")
    Call<List<RestaurantInfoDTO>> getRestaurants();
}
