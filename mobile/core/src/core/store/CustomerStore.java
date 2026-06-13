package com.example.core.store;

import androidx.lifecycle.MutableLiveData;

import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.dto.general.OrderDTO;
import com.example.core.dto.general.RestaurantInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CustomerStore {

    public static final MutableLiveData<CustomerDTO> customer = new MutableLiveData<>();
    public static final MutableLiveData<CusAddressResponseDTO> selectedAddress = new MutableLiveData<>();

    public static final MutableLiveData<List<CusAddressResponseDTO>> cusAddresses = new MutableLiveData<>();
    public static final MutableLiveData<List<CardResponseDTO>> cards = new MutableLiveData<>();

    public static final MutableLiveData<List<OrderDTO>> activeOrders = new MutableLiveData<>();

    public static final MutableLiveData<CartResponseDTO> cart = new MutableLiveData<>();


    public static final MutableLiveData<List<RestaurantInfoDTO>> restaurants = new MutableLiveData<>();

    public static final MutableLiveData<RestaurantInfoDTO> activeRestaurant = new MutableLiveData<>();


    public static CusAddressResponseDTO getAddress(Long addressId){
        return Objects.requireNonNull(CustomerStore.cusAddresses.getValue()).stream().filter(addr -> Objects.equals(addr.getAddressId(), addressId)).findFirst().get();
    }

    public static void deleteAddress(Long addressId){
        CustomerStore.cusAddresses.setValue(
                CustomerStore.cusAddresses.getValue().stream().filter(a-> a.getAddressId() != addressId).collect(Collectors.toList())
        );
    }

    public static void addAddress(CusAddressResponseDTO address){
        List<CusAddressResponseDTO> list = CustomerStore.cusAddresses.getValue();
        if (list != null) {
            list.add(address);
            CustomerStore.cusAddresses.setValue(list);
        }
    }

    public static void deleteCard(Long cardId){
        CustomerStore.cards.setValue(
                CustomerStore.cards.getValue().stream().filter(c->c.getCardId() != cardId).collect(Collectors.toList())
        );
    }

    public static CardResponseDTO getCard(Long cardId){
        return Objects.requireNonNull(CustomerStore.cards.getValue()).stream().filter(c-> Objects.equals(c.getCardId(), cardId)).findFirst().get();
    }

    public static void addCard(CardResponseDTO card){
        List<CardResponseDTO> list = CustomerStore.cards.getValue();
        if (list != null) {
            list.add(card);
            CustomerStore.cards.setValue(list);
        }
    }

}
