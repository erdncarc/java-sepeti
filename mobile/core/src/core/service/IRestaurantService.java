package com.example.core.service;

import com.example.core.dto.AddMenuItemRequest;
import com.example.core.dto.BusAddressRequest;
import com.example.core.dto.OpeningHourRequest;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.dto.restaurant.RestaurantDTO;
import com.example.core.dto.restaurant.UpdateMenuItemRequest;
import com.example.core.dto.restaurant.UpdateRestaurantInfoDTO;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRestaurantService {

    @PUT("me")
    Call<RestaurantDTO> updateRestaurantInfo(@Body UpdateRestaurantInfoDTO req);

    @POST("me/address")
    Call<RestaurantDTO> addAddress(@Body BusAddressRequest req);

    @POST("me/opening-hours")
    Call<RestaurantDTO> setOpeningHours(@Body List<OpeningHourRequest> req);

    @PUT("me/complete-registration")
    Call<RestaurantDTO> completeRegistration();

    @GET("me")
    Call<RestaurantDTO> getRestaurant();

    @GET("me/menu-items")
    Call<List<MenuItemDTO>> getMenuItems();

    @DELETE("me/menu-items/{menuItemId}")
    Call<MessageResponseDTO> deleteMenuItem(@Path("menuItemId") Long menuItemId);

    @POST("me/menu-items")
    Call<MenuItemDTO> addMenuItem(@Body AddMenuItemRequest req);

    @PUT("me/menu-items")
    Call<MenuItemDTO> updateMenuItem(@Body UpdateMenuItemRequest req);

    @PUT("me/photo")
    Call<RestaurantDTO> updateProfilePhoto(@Body String image);


}

