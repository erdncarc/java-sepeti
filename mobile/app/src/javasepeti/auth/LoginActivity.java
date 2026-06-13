package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.enums.UserRole;
import com.example.javasepeti.R;
import com.example.core.network.ApiClient;
import com.example.core.network.BaseCallback;
import com.example.core.repository.AuthRepository;
import com.example.javasepeti.courier.CourierMainActivity;
import com.example.javasepeti.customer.CustomerMainActivity;
import com.example.javasepeti.restaurant.RestaurantHomePageActivity;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends BaseAuthActivity {

    private EditText email;
    private EditText password;
    private CardView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.login = findViewById(R.id.login);

        this.login.setOnClickListener(v -> login());

        CardView button = findViewById(R.id.back_button);
        button.setOnClickListener(v-> finish());

        MaterialTextView signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(V -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


    }

    private void login() {
        Log.d("LoginActivity", "Login method called");
        String email =  this.email.getText().toString();
        String password = this.password.getText().toString();


        AuthRepository.getInstance().login(email, password, new BaseCallback<LoginResponseDTO>() {
            @Override
            public void onSuccess(LoginResponseDTO res) {
                Toast.makeText(LoginActivity.this ,"Login Successful", Toast.LENGTH_SHORT).show();
                /*
                if (res.getRole() == UserRole.CUSTOMER){
                    Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (res.getRole() == UserRole.RESTAURANT) {
                    Intent intent = new Intent(LoginActivity.this, RestaurantHomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else if (res.getRole() == UserRole.COURIER) {
                    Intent intent = new Intent(LoginActivity.this, CourierMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                */
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Login error: ", t);
            }
        });
    }
}