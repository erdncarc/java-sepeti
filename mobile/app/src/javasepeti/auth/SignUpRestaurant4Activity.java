package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.core.dto.OpeningHourRequest;
import com.example.core.dto.restaurant.RestaurantDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.RestaurantRepository;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.example.core.store.RestaurantStore;
import com.example.javasepeti.restaurant.RestaurantHomePageActivity;
import com.google.gson.JsonElement;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SignUpRestaurant4Activity extends BaseActivity {

    private EditText[] openInputs;
    private EditText[] closeInputs;
    private final String[] days = {
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
            "FRIDAY", "SATURDAY", "SUNDAY"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_restaurant_4);

        openInputs = new EditText[]{
                findViewById(R.id.open_mon),
                findViewById(R.id.open_tue),
                findViewById(R.id.open_wed),
                findViewById(R.id.open_thu),
                findViewById(R.id.open_fri),
                findViewById(R.id.open_sat),
                findViewById(R.id.open_sun)
        };

        closeInputs = new EditText[]{
                findViewById(R.id.close_mon),
                findViewById(R.id.close_tue),
                findViewById(R.id.close_wed),
                findViewById(R.id.close_thu),
                findViewById(R.id.close_fri),
                findViewById(R.id.close_sat),
                findViewById(R.id.close_sun)
        };

        for (EditText editText : openInputs){
            textListener(editText);
        }

        for (EditText editText : closeInputs){
            textListener(editText);
        }



        findViewById(R.id.next_button).setOnClickListener(v -> {
            List<OpeningHourRequest> openingHours = collectOpeningHours();

            RestaurantRepository.getInstance().setOpeningHours(openingHours, new BaseCallback<RestaurantDTO>() {
                @Override
                public void onSuccess(RestaurantDTO result) {
                    completeRegistration();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getApplicationContext(), "Opening hours not setted.", Toast.LENGTH_SHORT).show();
                }
            });
            for (OpeningHourRequest hour : openingHours) {
                Log.d("HOURS", hour.getDayOfWeek() + ": " + hour.getOpenTime() + " - " + hour.getCloseTime());
            }
        });
    }

    private void completeRegistration(){
        RestaurantRepository.getInstance().completeRegistration(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                Intent goToHome = new Intent(SignUpRestaurant4Activity.this, RestaurantHomePageActivity.class);
                startActivity(goToHome);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void textListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isEditing;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String input = s.toString().replace(":", "");
                if (input.length() > 4) input = input.substring(0, 4);

                String formatted = input;
                if (input.length() >= 3) {
                    formatted = input.substring(0, 2) + ":" + input.substring(2);

                    try {
                        int hour = Integer.parseInt(input.substring(0, 2));
                        int minute = Integer.parseInt(input.substring(2));

                        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                            editText.setError("Saat 00:00 – 23:59 aralığında olmalı");
                        } else {
                            editText.setError(null);
                        }
                    } catch (NumberFormatException e) {
                        editText.setError("Geçersiz saat");
                    }
                }

                editText.setText(formatted);
                editText.setSelection(formatted.length());

                isEditing = false;
            }
        });
    }

    private List<OpeningHourRequest> collectOpeningHours() {
        List<OpeningHourRequest> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < 7; i++) {
            String open = openInputs[i].getText().toString().trim();
            String close = closeInputs[i].getText().toString().trim();
            if (!open.isEmpty() && !close.isEmpty()) {
                OpeningHourRequest req = new OpeningHourRequest();
                req.setOpenTime(LocalTime.parse(open, formatter));
                req.setCloseTime(LocalTime.parse(close, formatter));
                req.setDayOfWeek(DayOfWeek.valueOf(days[i]));
                list.add(req);
            }else{
                OpeningHourRequest req = new OpeningHourRequest();
                req.setOpenTime(LocalTime.parse("09:00"));
                req.setCloseTime(LocalTime.parse("21:00"));
                req.setDayOfWeek(DayOfWeek.valueOf(days[i]));
                list.add(req);
            }
        }

        return list;
    }
}
