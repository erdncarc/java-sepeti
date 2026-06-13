package com.example.javasepeti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.enums.UserRole;
import com.example.core.network.ApiClient;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.repository.PublicRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.customer.ShowRestaurantActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchPageActivity extends AppCompatActivity {
    public static final String EXTRA_LAYOUT_ID = "layout_id";
    private final List<RestaurantInfoDTO> allRestaurants = new ArrayList<>();
    private int layoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        layoutId = getIntent().getIntExtra(EXTRA_LAYOUT_ID, R.layout.restaurant_card_anonymous);

        setListeners();
        setupSearchInput();

    }

    private void getRestaurantsFromCustomer(){
        if(CustomerStore.restaurants.getValue() != null && CustomerStore.restaurants.isInitialized()) {
            allRestaurants.clear();
            allRestaurants.addAll(CustomerStore.restaurants.getValue());
            displayRestaurantsCards(allRestaurants);
        }else{
            CustomerRepository.getInstance().getRestaurants(CustomerStore.selectedAddress.getValue().getAddressId(), new BaseCallback<List<RestaurantInfoDTO>>() {
                @Override
                public void onSuccess(List<RestaurantInfoDTO> result) {

                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    private void getRestaurantsFromAnonymous(){
        if(CustomerStore.restaurants.getValue() != null && CustomerStore.restaurants.isInitialized()) {
            allRestaurants.clear();
            allRestaurants.addAll(CustomerStore.restaurants.getValue());
            displayRestaurantsCards(allRestaurants);
        }else{
            PublicRepository.getInstance().getRestaurants(new BaseCallback<List<RestaurantInfoDTO>>() {
                @Override
                public void onSuccess(List<RestaurantInfoDTO> result) {
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    private void setListeners() {
        if(ApiClient.getLoginInfo().isInitialized() && ApiClient.getLoginInfo() != null && ApiClient.getLoginInfo().getValue().getRole() ==  UserRole.CUSTOMER){
            getRestaurantsFromCustomer();
            CustomerStore.restaurants.observe(this, new Observer<List<RestaurantInfoDTO>>() {
                @Override
                public void onChanged(List<RestaurantInfoDTO> restaurantInfoDTOS) {
                    getRestaurantsFromCustomer();
                }
            });
        }else{
            getRestaurantsFromAnonymous();
            CustomerStore.restaurants.observe(this, new Observer<List<RestaurantInfoDTO>>() {
                @Override
                public void onChanged(List<RestaurantInfoDTO> restaurantInfoDTOS) {
                    getRestaurantsFromAnonymous();
                }
            });
        }


        CardView button = findViewById(R.id.back_button);
        button.setOnClickListener(v-> finish());
    }

    private void setupSearchInput() {
        EditText searchInput = findViewById(R.id.search);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }
        });
    }

    private void filterRestaurants(String query) {
        if(query.isEmpty()){
            displayRestaurantsCards(allRestaurants);
            return;
        }
        List<RestaurantInfoDTO> filtered = new ArrayList<>();
        for (RestaurantInfoDTO r : allRestaurants) {
            String name = r.getRestaurantName();
            if (name != null && name.toLowerCase().contains(query.toLowerCase())) {
                if (r.getRestaurantName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(r);
                }
            }
        }
        displayRestaurantsCards(filtered);
    }

    @SuppressLint("DefaultLocale")
    private void displayRestaurantsCards(List<RestaurantInfoDTO> restaurants) {
        LinearLayout container = findViewById(R.id.restaurant_cards);
        LayoutInflater inflater = LayoutInflater.from(this);
        container.removeAllViews();

        for (RestaurantInfoDTO restaurant : restaurants) {
            View cardView = inflater.inflate(layoutId, container, false);

            TextView name = cardView.findViewById(R.id.text_restaurant_name);
            TextView star = cardView.findViewById(R.id.text_star);

            name.setText(restaurant.getRestaurantName());

            BigDecimal rating = restaurant.getRating() != null ? restaurant.getRating().total() : BigDecimal.ZERO;
            star.setText(String.format(Locale.US, "%.1f", rating.doubleValue()));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(SearchPageActivity.this, ShowRestaurantActivity.class);
                intent.putExtra("restaurant_id", restaurant.getUserId());
                startActivity(intent);
                //finish();
            });

            container.addView(cardView);
        }
    }
}
