package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.auth.RegisterDTO;
import com.example.core.enums.UserRole;
import com.example.core.network.ApiClient;
import com.example.core.network.BaseCallback;
import com.example.core.repository.AuthRepository;
import com.example.javasepeti.R;
import com.google.gson.JsonElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpRestaurant  extends BaseAuthActivity {
    private EditText name, email, password;
    private CheckBox policyCheckBox;
    private TextView loginText;

    private CardView signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_restaurant_1);

        name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        policyCheckBox = findViewById(R.id.policy);
        signupButton = findViewById(R.id.signup);
        loginText = findViewById(R.id.login);

        signupButton.setOnClickListener(v -> handleSignup());

        loginText.setOnClickListener(v -> startActivity(new Intent(SignUpRestaurant.this, LoginActivity.class)));

    }

    private void handleSignup() {
        String fullName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (fullName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(SignUpRestaurant.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(userEmail)) {
            Toast.makeText(SignUpRestaurant.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(SignUpRestaurant.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!policyCheckBox.isChecked()) {
            Toast.makeText(SignUpRestaurant.this, "You must accept the terms and privacy policy", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterDTO request = new RegisterDTO();
        request.setEmail(userEmail);
        request.setPassword(userPassword);
        request.setName(fullName);
        request.setRole(UserRole.RESTAURANT);
        AuthRepository.getInstance().register(request, new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                login(userEmail, userPassword);
            }

            @Override
            public void onError(Throwable t) {


                Toast.makeText(getApplicationContext(), "Sign Up Failed" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void nextStep(){
        Intent intent = new Intent(SignUpRestaurant.this, SignUpRestaurant2Activity.class);
        startActivity(intent);
        finish();
    }

    private void login(String  email, String password){
        AuthRepository.getInstance().login(email,password,new BaseCallback<LoginResponseDTO>() {
            @Override
            public void onSuccess(LoginResponseDTO result) {
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Login Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpRestaurant.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
