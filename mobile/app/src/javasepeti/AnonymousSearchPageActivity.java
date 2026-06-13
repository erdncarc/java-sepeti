package com.example.javasepeti;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.adapter.RestaurantAdapterAnonymous;
import com.example.javasepeti.fragment.SearchFragment;

public class AnonymousSearchPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anonymous_search_page); // XML dosyan bu adı taşıyor olmalı

        SearchFragment<RestaurantInfoDTO> searchFragment = new SearchFragment<>();

        // Adapter ayarlanıyor
        RestaurantAdapterAnonymous adapter = new RestaurantAdapterAnonymous(AnonymousSearchPageActivity.this);
        searchFragment.setAdapter(adapter);

        // LiveData kaynağı ayarlanıyor
        searchFragment.setLiveDataSource(CustomerStore.restaurants);

        // Filtreleme fonksiyonu (isim üzerinden)
        searchFragment.setSearchFunction(query -> restaurant -> {
            if (query == null || query.trim().isEmpty()) return true;

            String lowerQuery = query.toLowerCase();
            return (restaurant.getRestaurantName() != null && restaurant.getRestaurantName().toLowerCase().contains(lowerQuery))
                    || (restaurant.getDescription() != null && restaurant.getDescription().toLowerCase().contains(lowerQuery));
        });


        // Fragment sayfaya ekleniyor
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_fragment_container, searchFragment);
        transaction.commit();
    }
}
