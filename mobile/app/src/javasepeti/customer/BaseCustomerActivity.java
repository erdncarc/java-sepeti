package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.core.repository.AuthRepository;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.example.javasepeti.auth.BaseAuthActivity;
import com.example.javasepeti.auth.SignUpCustomer3;

public class BaseCustomerActivity extends BaseAuthActivity {


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ImageView view = findViewById(R.id.profile_button);
        if(view != null){
            view.setOnClickListener(v-> goToProfilePage());
        }

        ImageView view2 = findViewById(R.id.home_button);
        if(view2 != null){
            view2.setOnClickListener(v-> goToHomePage());
        }

        View basketBtn = findViewById(R.id.basket_button);
        if(basketBtn != null){
            basketBtn.setOnClickListener(v -> {
                openCart();
            });
        }

    }

    private void goToProfilePage(){
        Intent intent = new Intent(this, GeneralSettingsActivity.class);
        startActivity(intent);
    }


    private void goToHomePage(){
        Intent intent = new Intent(this, CustomerMainActivity.class);
        startActivity(intent);
    }


    private void openCart(){
        new BasketDialogFragment().show(getSupportFragmentManager(), "basket");
    }

}
