package com.example.javasepeti;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        CardView button = findViewById(R.id.back_button);
        if(button != null){
            button.setOnClickListener(v-> finish());
        }
    }






}
