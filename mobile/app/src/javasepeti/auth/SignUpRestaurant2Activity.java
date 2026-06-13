package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.dto.restaurant.RestaurantDTO;
import com.example.core.dto.restaurant.UpdateRestaurantInfoDTO;
import com.example.core.network.ApiClient;
import com.example.core.network.BaseCallback;
import com.example.core.repository.RestaurantRepository;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.example.core.store.RestaurantStore;

public class SignUpRestaurant2Activity extends BaseActivity {

    private EditText restaurantNameEditText, phoneNumberEditText, descriptionEditText;
    private TextView nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_restaurant_2);



        restaurantNameEditText = findViewById(R.id.restaurant_name);
        phoneNumberEditText = findViewById(R.id.phone_number);
        descriptionEditText = findViewById(R.id.description);
        nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> handleNext());
    }

    private void handleNext() {
        String restaurantName = restaurantNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(this, "Phone number must start with 5 and contain exactly 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateRestaurantInfoDTO req =  new UpdateRestaurantInfoDTO();
        req.setDescription(description);
        req.setPhone(phoneNumber);
        req.setRestaurantName(restaurantName);

        RestaurantRepository.getInstance().updateRestaurantInfo(req, new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void nextStep(){
        Intent intent = new Intent(SignUpRestaurant2Activity.this , SignUpRestaurant3Activity.class);
        startActivity(intent);
    }


    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^5\\d{9}$");
    }
}
