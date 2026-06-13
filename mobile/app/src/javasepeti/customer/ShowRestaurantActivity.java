package com.example.javasepeti.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.core.Utils;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.dto.restaurant.OpeningHourDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.PublicRepository;
import com.example.core.repository.RestaurantRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ShowRestaurantActivity extends BaseCustomerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_restaurant);

        long restaurantId = getIntent().getLongExtra("restaurant_id", -1);
        if (restaurantId == -1) {
            Toast.makeText(this, "Geçersiz restoran ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRestaurantById(restaurantId);



    }

    private void loadRestaurantById(long restaurantId) {
        List<RestaurantInfoDTO> storeRestaurants = CustomerStore.restaurants.getValue();

        if (CustomerStore.restaurants.isInitialized() && storeRestaurants != null) {
            RestaurantInfoDTO restaurant = findRestaurantById(storeRestaurants, restaurantId);
            if (restaurant != null) {
                populateUI(restaurant);
                return;
            }
        }

        PublicRepository.getInstance().getRestaurants(new BaseCallback<List<RestaurantInfoDTO>>() {
            @Override
            public void onSuccess(List<RestaurantInfoDTO> restaurants) {
                CustomerStore.restaurants.setValue(restaurants);
                RestaurantInfoDTO restaurant = findRestaurantById(restaurants, restaurantId);
                if (restaurant != null) {
                    populateUI(restaurant);
                } else {
                    Toast.makeText(ShowRestaurantActivity.this, "Restaurant not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(ShowRestaurantActivity.this, "Data could not be loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RestaurantInfoDTO findRestaurantById(List<RestaurantInfoDTO> restaurants, long id) {
        for (RestaurantInfoDTO r : restaurants) {
            if (r.getUserId() != null && r.getUserId() == id) {
                return r;
            }
        }
        return null;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void populateUI(RestaurantInfoDTO restaurant) {
        TextView name = findViewById(R.id.restaurant_name);
        TextView rate = findViewById(R.id.rate);
        TextView restaurant_rate = findViewById(R.id.restaurant_rate);
        TextView courier_rate = findViewById(R.id.courier_rate);
        TextView distance = findViewById(R.id.distance_value);
        TextView address = findViewById(R.id.address);
        TextView time = findViewById(R.id.time_value);

        TextView status = findViewById(R.id.open_status);

        name.setText(restaurant.getRestaurantName());
        rate.setText(String.format("%.1f", 5.00));
        restaurant_rate.setText(String.format("%.1f", 5.00));//restaurant.getRestaurantRate()
        courier_rate.setText(String.format("%.1f", 5.00));//restaurant.getCourierRate()
        distance.setText(String.valueOf(restaurant.getDistance() == null ? " - " : restaurant.getDistance()));//restaurant.getDistance()
        address.setText(restaurant.getBusAddress().addressToString());//restaurant.getAddress()
        OpeningHourDTO openingHourDTO =  Utils.getTodayOpeningHours(restaurant.getOpeningHours());

        if(openingHourDTO != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            time.setText(openingHourDTO.getOpenTime().format(formatter) + " - " + openingHourDTO.getCloseTime().format(formatter));//restaurant.getOpenTime() + " - " + restaurant.getCloseTime()
        }else{
            time.setText( "??:?? - ??:??");//restaurant.getOpenTime() + " - " + restaurant.getCloseTime()
        }

        status.setText(restaurant.isOpen() ? "OPEN": "CLOSE");
        if(restaurant.isOpen()){
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        }else{
            status.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }

        displayFoodItems(restaurant.getMenuItems());
    }

    private void displayFoodItems(List<MenuItemDTO> menuItems) {
        LayoutInflater inflater = LayoutInflater.from(this);
        GridLayout foodGrid = findViewById(R.id.food_grid);
        foodGrid.removeAllViews();

        for (MenuItemDTO menuItem : menuItems) {
            View card = inflater.inflate(R.layout.show_restaurant_food_card, foodGrid, false);

            TextView name = card.findViewById(R.id.food_name);
            TextView price = card.findViewById(R.id.food_price);
            TextView calorie = card.findViewById(R.id.food_calorie);
            TextView allergens = card.findViewById(R.id.food_allergens);
            TextView rating = card.findViewById(R.id.food_rating);

            name.setText(menuItem.getName());
            price.setText(String.format(Locale.US, "$ %.2f", menuItem.getPrice()));
            calorie.setText(String.format(Locale.US, "%d cal", menuItem.getCalorie()));
            allergens.setText(menuItem.getAllergens() != null ?
                    menuItem.getAllergens().stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")) : "");
            Double rating_val = menuItem.getRating() != null ? menuItem.getRating().doubleValue() : 5.00;
            rating.setText(String.format(Locale.US, "★ %.1f", rating_val));

            Gson gson = new Gson();
            String items = gson.toJson(menuItem);
            card.setOnClickListener(v -> {
                Intent intent = new Intent(ShowRestaurantActivity.this, ShowProductActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
                //finish();
            });

            foodGrid.addView(card);
        }
    }

}
