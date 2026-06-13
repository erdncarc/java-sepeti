package com.example.core.repository;

import android.content.Intent;
import android.widget.Toast;

import com.example.core.dto.AddMenuItemRequest;
import com.example.core.dto.BusAddressRequest;
import com.example.core.dto.OpeningHourRequest;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.dto.restaurant.RestaurantDTO;
import com.example.core.dto.restaurant.UpdateMenuItemRequest;
import com.example.core.dto.restaurant.UpdateRestaurantInfoDTO;
import com.example.core.network.BaseCallback;
import com.example.core.service.IRestaurantService;
import com.example.core.store.CustomerStore;
import com.example.core.store.RestaurantStore;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public class RestaurantRepository extends EntityRepository {

    @Getter
    private static final RestaurantRepository instance = new RestaurantRepository("restaurant");

    private final IRestaurantService service;

    private RestaurantRepository(String endpoint) {
        super(endpoint);
        this.service = this.retrofit.create(IRestaurantService.class);
    }


    public void updateRestaurantInfo(UpdateRestaurantInfoDTO req, BaseCallback<RestaurantDTO> callback){
        service.updateRestaurantInfo(req).enqueue(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                RestaurantStore.restaurant.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void  addAddress(BusAddressRequest req, BaseCallback<RestaurantDTO> callback){
        service.addAddress(req).enqueue(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                RestaurantStore.restaurant.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void setOpeningHours(List<OpeningHourRequest> req, BaseCallback<RestaurantDTO> callback){
        service.setOpeningHours(req).enqueue(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                RestaurantStore.restaurant.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void completeRegistration(BaseCallback<RestaurantDTO> callback){
        service.completeRegistration().enqueue(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                RestaurantStore.restaurant.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getRestaurant(BaseCallback<RestaurantDTO> callback){
        service.getRestaurant().enqueue(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                RestaurantStore.restaurant.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void getMenuItems(BaseCallback<List<MenuItemDTO>> callback){
        service.getMenuItems().enqueue(new BaseCallback<List<MenuItemDTO>>() {
            @Override
            public void onSuccess(List<MenuItemDTO> result) {
                RestaurantStore.menuItems.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteMenuItem(Long menuItemId, BaseCallback<MessageResponseDTO> callback){
        service.deleteMenuItem(menuItemId).enqueue(new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                RestaurantStore.deleteMenuItem(menuItemId);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void addMenuItem(AddMenuItemRequest req ,BaseCallback<MenuItemDTO> callback){
        service.addMenuItem(req).enqueue(new BaseCallback<MenuItemDTO>() {
            @Override
            public void onSuccess(MenuItemDTO result) {
                List<MenuItemDTO> list =  RestaurantStore.menuItems.getValue();
                if(list == null){
                    list = new ArrayList<>();
                }
                list.add(result);
                RestaurantStore.menuItems.setValue(list);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void updateMenuItem(UpdateMenuItemRequest req , BaseCallback<MenuItemDTO> callback){
        service.updateMenuItem(req).enqueue(new BaseCallback<MenuItemDTO>() {
            @Override
            public void onSuccess(MenuItemDTO result) {
                List<MenuItemDTO> list =  RestaurantStore.menuItems.getValue();
                if(list == null){
                    list = new ArrayList<>();
                }

                list = list.stream().filter(i -> i.getItemId() != result.getItemId()).collect(Collectors.toList());
                list.add(result);
                RestaurantStore.menuItems.setValue(list);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void updateProfilePhoto(String image , BaseCallback<RestaurantDTO> callback){
        service.updateProfilePhoto(image).enqueue(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                RestaurantStore.restaurant.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

}

