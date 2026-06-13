package com.example.javasepeti.customer;


import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.example.core.dto.FilterRestaurantRequest;
import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;
import com.example.javasepeti.adapter.RestaurantAdapterCustomer;
import com.example.javasepeti.fragment.SearchFragment;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomerSearchPageActivity extends BaseCustomerActivity implements FilterRestaurantDialogFragment.OnFilterApplyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anonymous_search_page); // XML dosyası içinde FragmentContainerView olmalı

        SearchFragment<RestaurantInfoDTO> searchFragment = new SearchFragment<>();

        // Adapter bağla
        RestaurantAdapterCustomer adapter = new RestaurantAdapterCustomer(this);
        searchFragment.setAdapter(adapter);

        // Veri kaynağı bağla
        searchFragment.setLiveDataSource(CustomerStore.restaurants);

        // Arama fonksiyonu tanımla
        searchFragment.setSearchFunction(query -> restaurant -> {
            if (query == null || query.trim().isEmpty()) return true;
            String lowerQuery = query.toLowerCase(Locale.US);
            return (restaurant.getRestaurantName() != null && restaurant.getRestaurantName().toLowerCase().contains(lowerQuery)) ||
                    (restaurant.getDescription() != null && restaurant.getDescription().toLowerCase().contains(lowerQuery));
        });

        // Fragment'ı sayfaya yerleştir
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_fragment_container, searchFragment);
        transaction.commit();
    }

    @Override
    public void onFilterApply(FilterRestaurantRequest req) {
        req.setLocation(CustomerStore.selectedAddress.getValue().getLocation());
        CustomerRepository.getInstance().filterRestaurants(req, new BaseCallback<List<RestaurantInfoDTO>>() {
            @Override
            public void onSuccess(List<RestaurantInfoDTO> result) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
