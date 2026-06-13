package com.example.core.service;


import com.example.core.dto.FilterRestaurantRequest;
import com.example.core.dto.MakeAnOrderRequest;
import com.example.core.dto.UpdateCardRequest;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.customer.AddCardRequest;
import com.example.core.dto.customer.AddCartItemRequest;
import com.example.core.dto.customer.AddCusAddressRequest;
import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.dto.customer.CustomerUpdateInfoRequest;
import com.example.core.dto.customer.UpdateCusAddressRequest;
import com.example.core.dto.general.OrderDTO;
import com.example.core.dto.general.RestaurantInfoDTO;
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

public interface ICustomerService{


    @PUT("me")
    Call<CustomerDTO> updateProfileInfo(@Body CustomerUpdateInfoRequest request);


    @PUT("me/photo")
    Call<CustomerDTO> updateProfilePhoto(@Body String base64image);

    @POST("me/addresses")
    Call<CusAddressResponseDTO> addAddress(@Body AddCusAddressRequest req);

    @POST("me/cards")
    Call<CardResponseDTO> addCard(@Body AddCardRequest req);

    @PUT("me/complete-registration")
    Call<CustomerDTO> completeRegistration();

    @GET("me/addresses")
    Call<List<CusAddressResponseDTO>> getAddresses();

    @GET("me")
    Call<CustomerDTO> getCustomer();

    @GET("me/order/active")
    Call<List<OrderDTO>> getActiveOrders();

    @DELETE("me/addresses")
    Call<MessageResponseDTO> deleteAddress(@Query("addressId") Long addressId);

    @PUT("me/addresses")
    Call<CusAddressResponseDTO> updateAddress(@Body UpdateCusAddressRequest req);

    @DELETE("me/cards")
    Call<MessageResponseDTO> deleteCard(@Query("cardId") Long cardId);

    @PUT("me/cards")
    Call<CardResponseDTO> updateCard(@Body UpdateCardRequest req);

    @GET("me/cards")
    Call<List<CardResponseDTO>> getCards();

    @GET("me/cart")
    Call<CartResponseDTO> getCart();

    @PUT("me/cart")
    Call<CartResponseDTO> updateCartItem(@Query("cartItemId") Long cartItemId, @Query("quantity") int quantity);

    @POST("me/cart")
    Call<CartResponseDTO> addItemToCart(@Body AddCartItemRequest req);

    @DELETE("me/cart")
    Call<CartResponseDTO> removeItemFromCart(@Query("cartItemId") Long cartItemId);

    @POST("me/order")
    Call<OrderDTO> makeAnOrder(@Body MakeAnOrderRequest req);

    @GET("restaurants")
    Call<List<RestaurantInfoDTO>> getRestaurants(@Query("addressId") Long addressId);

    @POST("restaurants/filter")
    Call<List<RestaurantInfoDTO>> filterRestaurants(@Body FilterRestaurantRequest req);




}
