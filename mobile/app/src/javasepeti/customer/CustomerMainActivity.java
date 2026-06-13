package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.core.Utils;
import com.example.core.dto.FilterRestaurantRequest;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.dto.general.OrderDTO;
import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.enums.AccountStatus;
import com.example.core.enums.OrderStatus;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.core.store.RestaurantStore;
import com.example.javasepeti.R;
import com.example.javasepeti.SearchPageActivity;
import com.example.javasepeti.auth.SignUpCustomer2;
import com.example.javasepeti.auth.SignUpCustomer3;
import com.example.javasepeti.service.CustomerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class CustomerMainActivity extends BaseCustomerActivity  {

    private CustomerService customerService = new CustomerService(CustomerMainActivity.this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);
        fetchAllInfo();

        check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        check();
    }

    private void fetchAllInfo(){
        if(!CustomerStore.cusAddresses.isInitialized()){
            CustomerRepository.getInstance().getAddresses(new BaseCallback<List<CusAddressResponseDTO>>() {
                @Override
                public void onSuccess(List<CusAddressResponseDTO> result) {
                    CustomerRepository.getInstance().getCustomer(new BaseCallback<CustomerDTO>() {
                        @Override
                        public void onSuccess(CustomerDTO result) {
                            check();
                        }

                        @Override
                        public void onError(Throwable t) {

                        }
                    });

                    CustomerRepository.getInstance().getRestaurants(CustomerStore.selectedAddress.getValue().getAddressId(), new BaseCallback<List<RestaurantInfoDTO>>() {
                        @Override
                        public void onSuccess(List<RestaurantInfoDTO> result) {

                        }

                        @Override
                        public void onError(Throwable t) {

                        }
                    });
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    private void check(){
        if(CustomerStore.customer.getValue() == null){
            fetchAllInfo();
            return;
        }

        if (!CustomerStore.customer.isInitialized() || CustomerStore.customer.getValue() == null) {
            return;
        }
        if (!CustomerStore.cusAddresses.isInitialized() || CustomerStore.cusAddresses.getValue() == null) {
            return;
        }

        CustomerDTO customer = CustomerStore.customer.getValue();

        if (customer.getAccountStatus() == AccountStatus.REGISTER) {
            if (customer.getImage() == null) {
                startActivity(new Intent(this, SignUpCustomer2.class));
                finish();
            } else if (CustomerStore.cusAddresses.getValue().isEmpty()) {
                startActivity(new Intent(this, SignUpCustomer3.class));
                finish();
            } else {
                startActivity(new Intent(this, SignUpCustomer3.class));
                finish();
            }
        } else {
            setContentView(R.layout.homepage_customer);
            setListeners();
            setCustomerFields();

            if (!CustomerStore.restaurants.isInitialized()) {
                CusAddressResponseDTO selected = CustomerStore.selectedAddress.getValue();
                if (selected != null) {
                    CustomerRepository.getInstance().getRestaurants(selected.getAddressId(), new BaseCallback<List<RestaurantInfoDTO>>() {
                        @Override
                        public void onSuccess(List<RestaurantInfoDTO> result) {
                            CustomerStore.restaurants.setValue(result);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(CustomerMainActivity.this, "Failed to fetch restaurants", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                displayRestaurantsCards(CustomerStore.restaurants.getValue());
            }

            getActiveOrders();

            CardView searchButton = findViewById(R.id.search_group);
            searchButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, CustomerSearchPageActivity.class);
                intent.putExtra(SearchPageActivity.EXTRA_LAYOUT_ID, R.layout.restaurant_card_customer);
                startActivity(intent);
            });
        }
    }



    private void displayRestaurantsCards(List<RestaurantInfoDTO> restaurants) {
        LinearLayout container = findViewById(R.id.recycler_restaurants_linear);
        LayoutInflater inflater = LayoutInflater.from(this);
        container.removeAllViews();

        if (restaurants == null || restaurants.isEmpty()) {
            Log.w("CustomerMainActivity", "Restaurant list is null or empty");
            return;
        }

        for (RestaurantInfoDTO restaurant : restaurants) {
            View cardView = inflater.inflate(R.layout.restaurant_card_customer, container, false); // layoutId yerine doğrudan XML dosyası

            TextView name = cardView.findViewById(R.id.text_restaurant_name);
            TextView star = cardView.findViewById(R.id.text_star);
            TextView state = cardView.findViewById(R.id.text_state);
            ImageView imageRestaurant =  findViewById(R.id.pp_restaurant);

            if(restaurant.getImage() != null){
                //imageRestaurant.setImageBitmap(Utils.getImageAsBitmap(restaurant.getImage()));
            }
            state.setText(restaurant.isOpen() ? "Open" : "Closed");
            name.setText(restaurant.getRestaurantName());
            BigDecimal rating = restaurant.getRating() != null ? restaurant.getRating().total() : BigDecimal.ZERO;
            star.setText(String.format(Locale.US, "%.1f", rating.doubleValue()));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(CustomerMainActivity.this, ShowRestaurantActivity.class);
                intent.putExtra("restaurant_id", restaurant.getUserId());
                startActivity(intent);
                //finish();
            });

            container.addView(cardView);
        }
    }

    private void displayActiveOrders(List<OrderDTO> orders) {
        LinearLayout container = findViewById(R.id.recycler_orders);
        LayoutInflater inflater = LayoutInflater.from(this);
        container.removeAllViews();



        if(orders.isEmpty()){
            View view = inflater.inflate(R.layout.no_order_box, container, false); // layoutId yerine doğrudan XML dosyası
            container.addView(view);
            return;
        }

        for (OrderDTO order : orders) {
            View view = inflater.inflate(R.layout.active_order_box, container, false); // layoutId yerine doğrudan XML dosyası

            TextView name = view.findViewById(R.id.restaurant_name);
            TextView address = view.findViewById(R.id.address);

            TextView state1 = view.findViewById(R.id.received_text);
            ImageView check1 = view.findViewById(R.id.received_check);
            ImageView check2 = view.findViewById(R.id.preparation_check);
            ImageView check3 = view.findViewById(R.id.delivery_check);
            ImageView check4 = view.findViewById(R.id.delivered_check);



            TextView state2 = view.findViewById(R.id.preparation_text);
            TextView state3 = view.findViewById(R.id.delivery_text);
            TextView state4 = view.findViewById(R.id.delivered_text);

            if( order.getStatus() == OrderStatus.RECEIVED){
                state1.setTextColor(ContextCompat.getColor(this, R.color.green));
                check1.setColorFilter(ContextCompat.getColor(this, R.color.green));
            } else if (order.getStatus() == OrderStatus.PREPERATION) {
                state1.setTextColor(ContextCompat.getColor(this, R.color.green));
                check1.setColorFilter(ContextCompat.getColor(this, R.color.green));
                state2.setTextColor(ContextCompat.getColor(this, R.color.green));
                check2.setColorFilter(ContextCompat.getColor(this, R.color.green));
            } else if (order.getStatus() == OrderStatus.ON_DELIVERY) {
                state1.setTextColor(ContextCompat.getColor(this, R.color.green));
                check1.setColorFilter(ContextCompat.getColor(this, R.color.green));
                state2.setTextColor(ContextCompat.getColor(this, R.color.green));
                check2.setColorFilter(ContextCompat.getColor(this, R.color.green));
                state3.setTextColor(ContextCompat.getColor(this, R.color.green));
                check3.setColorFilter(ContextCompat.getColor(this, R.color.green));
            }else{
                state1.setTextColor(ContextCompat.getColor(this, R.color.green));
                check1.setColorFilter(ContextCompat.getColor(this, R.color.green));
                state2.setTextColor(ContextCompat.getColor(this, R.color.green));
                check2.setColorFilter(ContextCompat.getColor(this, R.color.green));
                state3.setTextColor(ContextCompat.getColor(this, R.color.green));
                check3.setColorFilter(ContextCompat.getColor(this, R.color.green));
                state4.setTextColor(ContextCompat.getColor(this, R.color.green));
                check4.setColorFilter(ContextCompat.getColor(this, R.color.green));
            }



            name.setText(order.getCart().getRestaurant().getRestaurantName());
            address.setText(order.getAddress().addressToString());

            container.addView(view);
        }
    }


    public void setListeners(){


        CustomerStore.restaurants.observe(this, new Observer<List<RestaurantInfoDTO>>() {
            @Override
            public void onChanged(List<RestaurantInfoDTO> showRestaurants) {
                listRestaurants();
            }
        });


        CustomerStore.restaurants.observe(this, new Observer<List<RestaurantInfoDTO>>() {
            @Override
            public void onChanged(List<RestaurantInfoDTO> showRestaurants) {
                if(showRestaurants == null || showRestaurants.isEmpty()){
                    return;
                }
                displayRestaurantsCards(showRestaurants);
            }
        });

        CustomerStore.activeOrders.observe(this, new Observer<List<OrderDTO>>() {
            @Override
            public void onChanged(List<OrderDTO> orders) {
                displayActiveOrders(orders);
            }
        });

    }


    private void setCustomerFields(){
        ImageView pp = findViewById(R.id.pp);
        String imageStr = CustomerStore.customer.getValue().getImage();
        if(imageStr != null ){
            pp.setImageBitmap(Utils.getImageAsBitmap(imageStr));
        }

        TextView customer_name  = findViewById(R.id.customer_name);
        customer_name.setText(CustomerStore.customer.getValue().getName());

        TextView lp_points = findViewById(R.id.lp_field);

        lp_points.setText(String.format("%.2f",CustomerStore.customer.getValue().getLoyPoint()));


        TextView locationTitle = findViewById(R.id.address_title);
        TextView locationText = findViewById(R.id.address_text);
        if(!CustomerStore.selectedAddress.isInitialized()){
            if(!CustomerStore.cusAddresses.getValue().isEmpty()){
                CusAddressResponseDTO address =  CustomerStore.cusAddresses.getValue().getFirst();
                locationTitle.setText(address.getTitle());
                locationText.setText(address.addressToString());
            }
            else{
                // fetch addresses
            }
        }else{
            locationTitle.setText(CustomerStore.selectedAddress.getValue().getTitle());
            locationText.setText(CustomerStore.selectedAddress.getValue().addressToString());
        }
    }

    private void listRestaurants(){
        //this.restaurantAdapter.setItems(CustomerStore.restaurants.getValue());
    }

    public void getActiveOrders(){
        CustomerRepository.getInstance().getActiveOrders(new BaseCallback<List<OrderDTO>>() {
            @Override
            public void onSuccess(List<OrderDTO> orders) {
                if(!orders.isEmpty()){
                    ConstraintLayout layout = findViewById(R.id.no_order_box_c);
                    if(layout != null){
                        layout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(CustomerMainActivity.this, "An error occured",Toast.LENGTH_SHORT).show();

            }
        });
    }




}
