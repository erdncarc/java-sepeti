package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.auth.RegisterDTO;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.dto.customer.CustomerUpdateInfoRequest;
import com.example.core.enums.UserRole;
import com.example.core.network.BaseCallback;
import com.example.core.repository.AuthRepository;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

public class SignUpCustomer1 extends BaseAuthActivity {

    EditText fullNameField, phoneField, emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_customer_1);


        TextView login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpCustomer1.this, LoginActivity.class);
            startActivity(intent);
        });

        // EditText'leri tanımla
        fullNameField = findViewById(R.id.full_name);
        phoneField = findViewById(R.id.phone_number);
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
        req.setRole(UserRole.CUSTOMER);
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
                update();
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Login Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpCustomer1.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void nextStep(){
        Intent intent = new Intent(SignUpCustomer1.this, SignUpCustomer2.class);
        startActivity(intent);
        finish();
    }

    private void update(){
        String fullName = fullNameField.getText().toString();
        String phone = phoneField.getText().toString().trim();
        CustomerUpdateInfoRequest req =  new CustomerUpdateInfoRequest();
        req.setName(fullName);
        req.setPhone(phone);

        CustomerRepository.getInstance().updateProfileInfo(req, new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Update Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
