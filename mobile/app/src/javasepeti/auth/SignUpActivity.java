package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.javasepeti.R;

public class SignUpActivity extends BaseAuthActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_select);


        TextView signup = findViewById(R.id.signup);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        LinearLayout customerCard = findViewById(R.id.customer_card);
        customerCard.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignUpCustomer1.class);
            startActivity(intent);
        });

        LinearLayout restaurant_card = findViewById(R.id.restaurant_card);
        restaurant_card.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignUpRestaurant.class);
            startActivity(intent);
        });

        LinearLayout courier_card = findViewById(R.id.courier_card);
        courier_card.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignUpCourier1.class);
            startActivity(intent);
        });


    }
}
