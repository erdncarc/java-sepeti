package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.auth.RegisterDTO;
import com.example.core.enums.UserRole;
import com.example.core.network.BaseCallback;
import com.example.core.repository.AuthRepository;
import com.example.javasepeti.R;

public class SignUpCourier1 extends BaseAuthActivity{

    EditText fullNameField, emailField, passwordField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_courier_1);

        TextView login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpCourier1.this, LoginActivity.class);
            startActivity(intent);
        });

        fullNameField = findViewById(R.id.full_name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        CardView signup = findViewById(R.id.signup);
        signup.setOnClickListener(v -> {
            register();
        });


    }


    private void register(){
        String fullName = fullNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();


        RegisterDTO req = new RegisterDTO();
        req.setRole(UserRole.COURIER);
        req.setName(fullName);
        req.setEmail(email);
        req.setPassword(password);

        AuthRepository.getInstance().register(req, new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                login(req.getEmail(), req.getPassword());
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Sign Up Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void login(String  email, String password){
        AuthRepository.getInstance().login(email,password,new BaseCallback<LoginResponseDTO>() {
            @Override
            public void onSuccess(LoginResponseDTO result) {
                getProfile();
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Login Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpCourier1.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void getProfile(){
        /*
        CourierRepository.getInstance().getProfile(new BaseCallback<Courier>() {
            @Override
            public void onSuccess(Courier result) {
                CourierStore.courier.setValue(result);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Get Profile Failed: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Profile info did not fetched." + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        */

    }

    private void nextStep(){
        Intent intent = new Intent(SignUpCourier1.this, SignUpCourier2.class);
        startActivity(intent);
        finish();
    }
}
