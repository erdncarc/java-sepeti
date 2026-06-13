package com.example.core.service;

import com.example.core.dto.auth.LoginRequestDTO;
import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.auth.RegisterDTO;
import com.example.core.dto.general.UserDTO;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthService {
    @POST("login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO request);

    @POST("register")
    Call<MessageResponseDTO> register(@Body RegisterDTO request);
} 