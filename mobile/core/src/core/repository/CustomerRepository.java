package com.example.core.repository;

import android.util.Log;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

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
import com.example.core.dto.general.CartItemDTO;
import com.example.core.dto.general.OrderDTO;
import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.network.BaseCallback;
import com.example.core.service.ICustomerService;
import com.example.core.store.CustomerStore;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Objects;

import kotlin.reflect.KCallable;
import lombok.Getter;

public class CustomerRepository extends EntityRepository {

    @Getter
    private static final CustomerRepository instance = new CustomerRepository("customer");

    private final ICustomerService service;

    private CustomerRepository(String endpoint) {
        super(endpoint);
        this.service = this.retrofit.create(ICustomerService.class);
    }


    public void updateProfileInfo(CustomerUpdateInfoRequest req, BaseCallback<CustomerDTO> callback){
        service.updateProfileInfo(req).enqueue(new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                CustomerStore.customer.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Customer Update Info failed: " + t.getMessage());
                t.addSuppressed(new Exception("Customer Update Info failed"));
                callback.onError(t);
            }
        });
    }

    public void updateProfilePhoto(String base64image, BaseCallback<CustomerDTO> callback){
        service.updateProfilePhoto(base64image).enqueue(new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                CustomerStore.customer.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Customer Update Photo failed: " + t.getMessage());
                t.addSuppressed(new Exception("Customer Update Photo failed"));
                callback.onError(t);
            }
        });
    }


    public void addAddress(AddCusAddressRequest req, BaseCallback<CusAddressResponseDTO> callback){
        service.addAddress(req).enqueue(new BaseCallback<CusAddressResponseDTO>() {
            @Override
            public void onSuccess(CusAddressResponseDTO result) {
                List<CusAddressResponseDTO> list = CustomerStore.cusAddresses.getValue();
                if (list != null) {
                    list.add(result);
                    CustomerStore.cusAddresses.setValue(list);
                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Customer Add Address failed: " + t.getMessage());
                t.addSuppressed(new Exception("Customer Add Address failed"));
                callback.onError(t);
            }
        });
    }

    public void completeRegister(BaseCallback<CustomerDTO> callback){
        service.completeRegistration().enqueue(new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                CustomerStore.customer.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                Log.d("API_ERROR", t.getMessage(), t);
                t.addSuppressed(new Exception("Customer Complete Registration failed"));
                callback.onError(t);
            }
        });
    }

    public void addCard(AddCardRequest req, BaseCallback<CardResponseDTO> callback){
        service.addCard(req).enqueue(new BaseCallback<CardResponseDTO>() {
            @Override
            public void onSuccess(CardResponseDTO result) {
                List<CardResponseDTO> list = CustomerStore.cards.getValue();
                if (list != null) {
                    list.add(result);
                    CustomerStore.cards.setValue(list);
                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void getAddresses(BaseCallback<List<CusAddressResponseDTO>> callback){
        service.getAddresses().enqueue(new BaseCallback<List<CusAddressResponseDTO>>() {
            @Override
            public void onSuccess(List<CusAddressResponseDTO> result) {
                CustomerStore.cusAddresses.setValue(result);

                if(!result.isEmpty()){
                    if(CustomerStore.selectedAddress.getValue() == null || !CustomerStore.selectedAddress.isInitialized() ){
                        CustomerStore.selectedAddress.setValue(CustomerStore.cusAddresses.getValue().getFirst());
                    }else{
                        CusAddressResponseDTO address =  CustomerStore.cusAddresses.getValue().stream().filter(a -> a.getAddressId() == CustomerStore.selectedAddress.getValue().getAddressId()).findFirst().get();
                        if(address == null){
                            CustomerStore.selectedAddress.setValue(CustomerStore.cusAddresses.getValue().getFirst());
                        }
                    }
                }


                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getCustomer(BaseCallback<CustomerDTO> callback){
        service.getCustomer().enqueue(new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                CustomerStore.customer.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getActiveOrders(BaseCallback<List<OrderDTO>> callback){
        service.getActiveOrders().enqueue(new BaseCallback<List<OrderDTO>>() {
            @Override
            public void onSuccess(List<OrderDTO> result) {
                CustomerStore.activeOrders.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteAddress(Long addressId, BaseCallback<MessageResponseDTO> callback){
        service.deleteAddress(addressId).enqueue(new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                CustomerStore.deleteAddress(addressId);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void updateAddress(UpdateCusAddressRequest req, BaseCallback<CusAddressResponseDTO> callback){
        service.updateAddress(req).enqueue(new BaseCallback<CusAddressResponseDTO>() {
            @Override
            public void onSuccess(CusAddressResponseDTO result) {
                CustomerStore.deleteAddress(req.getAddressId());
                CustomerStore.addAddress(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteCard(Long cardId, BaseCallback<MessageResponseDTO> callback){
        service.deleteCard(cardId).enqueue(new BaseCallback<>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                CustomerStore.deleteCard(cardId);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void updateCard(UpdateCardRequest req, BaseCallback<CardResponseDTO> callback){
        service.updateCard(req).enqueue(new BaseCallback<>() {
            @Override
            public void onSuccess(CardResponseDTO result) {
                CustomerStore.deleteCard(result.getCardId());
                CustomerStore.addCard(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getCards(BaseCallback<List<CardResponseDTO>> callback){
        service.getCards().enqueue(new BaseCallback<List<CardResponseDTO>>() {
            @Override
            public void onSuccess(List<CardResponseDTO> result) {
                CustomerStore.cards.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void getCart(BaseCallback<CartResponseDTO> callback){
        service.getCart().enqueue(new BaseCallback<CartResponseDTO>() {
            @Override
            public void onSuccess(CartResponseDTO result) {
                CustomerStore.cart.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void updateCartItem(Long cartItemId, int quantity, BaseCallback<CartResponseDTO> callback){
        service.updateCartItem(cartItemId,quantity).enqueue(new BaseCallback<CartResponseDTO>() {
            @Override
            public void onSuccess(CartResponseDTO result) {
                CustomerStore.cart.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void removeItemFromCart(Long cartItemId, BaseCallback<CartResponseDTO> callback){
        service.removeItemFromCart(cartItemId).enqueue(new BaseCallback<CartResponseDTO>() {
            @Override
            public void onSuccess(CartResponseDTO result) {
                CustomerStore.cart.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void makeAnOrder(MakeAnOrderRequest req, BaseCallback<OrderDTO> callback){
        service.makeAnOrder(req).enqueue(new BaseCallback<OrderDTO>() {
            @Override
            public void onSuccess(OrderDTO result) {
                // Customer store order info operations
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getRestaurants(Long addressId, BaseCallback<List<RestaurantInfoDTO>> callback){
        service.getRestaurants(addressId).enqueue(new BaseCallback<List<RestaurantInfoDTO>>() {
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

    public void addItemToCart(AddCartItemRequest req, BaseCallback<CartResponseDTO> callback){
        service.addItemToCart(req).enqueue(new BaseCallback<CartResponseDTO>() {
            @Override
            public void onSuccess(CartResponseDTO result) {
                CustomerStore.cart.setValue(result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void filterRestaurants(FilterRestaurantRequest req, BaseCallback<List<RestaurantInfoDTO>> callback){
        service.filterRestaurants(req).enqueue(new BaseCallback<List<RestaurantInfoDTO>>() {
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


}

