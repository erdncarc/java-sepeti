package com.example.javasepeti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.PublicRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.auth.BaseAuthActivity;
import com.example.javasepeti.auth.LoginActivity;
import com.example.javasepeti.customer.ShowRestaurantActivity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;


public class HomepageAnonymousActivity extends BaseAuthActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_anonymous);

        setListeners();
        loadRestaurantsFromStoreOrApi();

    }

    private void setListeners() {
        ImageView profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> goToSign());

        CardView searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageAnonymousActivity.this, AnonymousSearchPageActivity.class);
            intent.putExtra(SearchPageActivity.EXTRA_LAYOUT_ID, R.layout.restaurant_card_anonymous);
            startActivity(intent);
            //finish();
        });

        CustomerStore.restaurants.observe(this, restaurants -> {
            loadRestaurantsFromStoreOrApi();
        });
    }

    private void loadRestaurantsFromStoreOrApi() {
        if(!CustomerStore.restaurants.isInitialized()){
            PublicRepository.getInstance().getRestaurants(new BaseCallback<>() {
                @Override
                public void onSuccess(List<RestaurantInfoDTO> restaurants) {
                }

                @Override
                public void onError(Throwable t) {
                    Log.e("SearchPage", "Data could not be retrieved", t);
                }
            });
        }else{
            displayRestaurantsCards(CustomerStore.restaurants.getValue());
        }


    }

    private void displayRestaurantsCards(List<RestaurantInfoDTO> restaurants) {
        LinearLayout container = findViewById(R.id.recycler_restaurants);
        LayoutInflater inflater = LayoutInflater.from(this);
        container.removeAllViews();

        if(restaurants == null){
            return;
        }

        for (RestaurantInfoDTO restaurant : restaurants) {
            View cardView = inflater.inflate(R.layout.restaurant_card_anonymous, container, false); // layoutId yerine doğrudan XML dosyası

            TextView name = cardView.findViewById(R.id.text_restaurant_name);
            TextView star = cardView.findViewById(R.id.text_star);
            TextView state = cardView.findViewById(R.id.text_state);

            state.setText("Open");
            name.setText(restaurant.getRestaurantName());
            BigDecimal rating = restaurant.getRating() != null ? restaurant.getRating().total() : BigDecimal.ZERO;
            star.setText(String.format(Locale.US, "%.1f", rating.doubleValue()));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(HomepageAnonymousActivity.this, ShowRestaurantActivity.class);
                intent.putExtra("restaurant_id", restaurant.getUserId());
                startActivity(intent);
                //finish();
            });

            container.addView(cardView);
        }
    }

    private void goToSign() {
        Intent intent = new Intent(HomepageAnonymousActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}