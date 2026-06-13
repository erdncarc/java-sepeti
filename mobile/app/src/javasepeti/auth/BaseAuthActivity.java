package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.core.dto.auth.LoginResponseDTO;
import com.example.core.enums.UserRole;
import com.example.core.network.ApiClient;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.HomepageAnonymousActivity;
import com.example.javasepeti.courier.CourierMainActivity;
import com.example.javasepeti.customer.CustomerMainActivity;
import com.example.javasepeti.restaurant.RestaurantHomePageActivity;

public class BaseAuthActivity extends BaseActivity {

    public static MutableLiveData<LoginResponseDTO> state = new MutableLiveData<>();

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ApiClient.getLoginInfo().observe(this, new Observer<LoginResponseDTO>() {
            @Override
            public void onChanged(LoginResponseDTO loginResponseDTO) {
                auth(loginResponseDTO);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //auth(ApiClient.getLoginInfo().getValue());
    }


    private void auth(LoginResponseDTO loginResponseDTO) {
        // Aynı role sahipse tekrar yönlendirme yapma
        if (state.getValue() != null && loginResponseDTO != null) {
            if (state.getValue().getRole() == loginResponseDTO.getRole()) {
                return;
            }
        }

        if(state.getValue() == null && loginResponseDTO == null){
            return;
        }


        // loginResponse null ise anonim sayfaya gönder
        if (loginResponseDTO == null) {
            if (!(this instanceof HomepageAnonymousActivity)) {
                Intent intent = new Intent(getApplicationContext(), HomepageAnonymousActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            state.setValue(null);
            return;
        }

        // Diğer rollere göre yönlendirme
        Intent intent = null;
        if (loginResponseDTO.getRole().equals(UserRole.CUSTOMER)) {
            intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
        } else if (loginResponseDTO.getRole().equals(UserRole.RESTAURANT)) {
            intent = new Intent(getApplicationContext(), RestaurantHomePageActivity.class);
        } else if (loginResponseDTO.getRole().equals(UserRole.COURIER)) {
            intent = new Intent(getApplicationContext(), CourierMainActivity.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            state.setValue(loginResponseDTO);
        }
    }

}
