package com.example.core.repository;

import android.content.Intent;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.network.ApiClient;
import com.example.core.network.BaseCallback;
import com.example.core.service.IPublicService;
import com.example.core.store.CustomerStore;
import com.example.core.store.RestaurantStore;

import java.util.List;

import lombok.Getter;

public class PublicRepository extends EntityRepository {

    @Getter
    private static final PublicRepository instance = new PublicRepository("public");
    IPublicService service;
    public PublicRepository(String endpoint) {
        super(endpoint);
        this.service = retrofit.create(IPublicService.class);
    }

    public void getRestaurants(BaseCallback<List<RestaurantInfoDTO>> callback){
        service.getRestaurants().enqueue(new BaseCallback<List<RestaurantInfoDTO>>() {
            @Override
            public void onSuccess(List<RestaurantInfoDTO> result) {
                CustomerStore.restaurants.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void signOut(){
        CustomerStore.restaurants.setValue(null);
        CustomerStore.customer.setValue(null);
        CustomerStore.cart.setValue(null);
        CustomerStore.cusAddresses.setValue(null);
        CustomerStore.selectedAddress.setValue(null);
        CustomerStore.cards.setValue(null);

        RestaurantStore.menuItems.setValue(null);
        RestaurantStore.restaurant.setValue(null);

        ApiClient.getLoginInfo().setValue(null);
    }

}
