package com.example.core.repository;

import android.util.Log;

import com.example.core.dto.auth.LoginRequestDTO;
import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.auth.RegisterDTO;
import com.example.core.network.ApiClient;
import com.example.core.network.BaseCallback;
import com.example.core.service.IAuthService;
import com.example.core.store.CustomerStore;
import com.google.gson.JsonElement;

import lombok.Getter;

public class AuthRepository extends EntityRepository {

    @Getter
    private static final AuthRepository instance = new AuthRepository("public/auth");

    private final IAuthService service;

    private AuthRepository(String endpoint) {
        super(endpoint);
        this.service = this.retrofit.create(IAuthService.class);
    }

    public void login(String email, String password, BaseCallback<LoginResponseDTO> callback) {

        service.login(new LoginRequestDTO(email,password)).enqueue(new BaseCallback<LoginResponseDTO>() {
            @Override
            public void onSuccess(LoginResponseDTO result) {
                ApiClient.getLoginInfo().setValue(result);
                CustomerStore.restaurants.setValue(null);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Giriş Başarısız: " + t.getMessage());
                callback.onError(t);
            }
        });
    }

    public void register(RegisterDTO request, BaseCallback<MessageResponseDTO> callback) {
        service.register(request).enqueue(callback);
    }

    public void registerAndLogin(RegisterDTO request, BaseCallback<LoginResponseDTO> callback){
        register(request, new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                login(request.getPassword(), request.getEmail(), callback);
            }

            @Override
            public void onError(Throwable t) {
            }
        });
    }
} 