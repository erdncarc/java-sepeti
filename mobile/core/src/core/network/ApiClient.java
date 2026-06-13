package com.example.core.network;

import androidx.lifecycle.MutableLiveData;

import com.example.core.adapter.LocalTimeAdapter;
import com.example.core.deserializer.LocalTimeDeserializer;
import com.example.core.deserializer.LocalDateTimeDeserializer;
import com.example.core.dto.auth.LoginResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalTime;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    @Getter
    @Setter
    private static MutableLiveData<LoginResponseDTO> loginInfo = new MutableLiveData<>();

    public static Retrofit getClient(String endpoint) {


        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                //.registerTypeAdapter(BusAddress.class, new BusAddressDeserializer())
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request.Builder builder = original.newBuilder();

                    // Güvenli kontrol
                    LoginResponseDTO info = loginInfo.getValue();
                    if (info != null && info.getToken() != null) {
                        builder.header("Authorization", "Bearer " + info.getToken());
                    }

                    return chain.proceed(builder.build());
                })
                .addInterceptor(logging)
                .build();


        return new Retrofit.Builder()
                .baseUrl(BASE_URL + endpoint + "/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
