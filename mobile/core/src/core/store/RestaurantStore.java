package com.example.core.store;

import androidx.lifecycle.MutableLiveData;

import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.dto.restaurant.RestaurantDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RestaurantStore {

    public static final MutableLiveData<RestaurantDTO> restaurant = new MutableLiveData<>();
    public static final MutableLiveData<List<MenuItemDTO>> menuItems = new MutableLiveData<>();


    public static MenuItemDTO getMenuItem(Long menuItemId){
        return Objects.requireNonNull(RestaurantStore.menuItems.getValue()).stream().filter(m -> m.getItemId() == menuItemId).findFirst().get();
    }

    public static void deleteMenuItem(Long menuItemId){
        RestaurantStore.menuItems.setValue(
                RestaurantStore.menuItems.getValue().stream().filter(m -> m.getItemId() != menuItemId).collect(Collectors.toList())
        );
    }

    public static void addAddress(MenuItemDTO menuItemDTO){
        List<MenuItemDTO> list = RestaurantStore.menuItems.getValue();
        if (list != null) {
            list.add(menuItemDTO);
            RestaurantStore.menuItems.setValue(list);
        }
    }


}
