package com.example.javasepeti.restaurant;

import static com.example.core.store.RestaurantStore.restaurant;
import static com.example.core.store.RestaurantStore.menuItems;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalTime;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.core.Utils;
import com.example.core.dto.general.BusAddressDTO;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.dto.restaurant.OpeningHourDTO;
import com.example.core.dto.restaurant.RestaurantDTO;
import com.example.core.dto.restaurant.UpdateRestaurantInfoDTO;
import com.example.core.enums.AccountStatus;
import com.example.core.network.BaseCallback;
import com.example.core.repository.RestaurantRepository;
import com.example.core.store.CustomerStore;
import com.example.core.store.RestaurantStore;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.example.javasepeti.auth.SignUpRestaurant2Activity;
import com.example.javasepeti.auth.SignUpRestaurant3Activity;
import com.example.javasepeti.auth.SignUpRestaurant4Activity;
import com.google.gson.Gson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RestaurantHomePageActivity extends BaseRestaurantActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView imageView;
    private String base64Image = null;

    private boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListeners();
        if(!restaurant.isInitialized()){
            getRestaurant();
        }

        setContentView(R.layout.activity_loading);
    }


    private void checkAccount(){
        RestaurantDTO info  = restaurant.getValue();
        if(restaurant.isInitialized()){
            if(restaurant.getValue().getAccountStatus() == AccountStatus.REGISTER){
                if(restaurant.getValue().getPhone() == null){
                    Intent intent = new Intent(RestaurantHomePageActivity.this, SignUpRestaurant2Activity.class);
                    startActivity(intent);
                    finish();
                } else if (restaurant.getValue().getBusAddress() == null) {
                    Intent intent = new Intent(RestaurantHomePageActivity.this, SignUpRestaurant3Activity.class);
                    startActivity(intent);
                    finish();
                } else if (restaurant.getValue().getOpeningHours().isEmpty()) {
                    Intent intent = new Intent(RestaurantHomePageActivity.this, SignUpRestaurant4Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    RestaurantRepository.getInstance().completeRegistration(new BaseCallback<RestaurantDTO>() {
                        @Override
                        public void onSuccess(RestaurantDTO result) {

                        }

                        @Override
                        public void onError(Throwable t) {

                        }
                    });
                }
            }else{
                if(!state){
                    setContentView(R.layout.homepage_restaurant);
                    imageView = findViewById(R.id.restaurant_image);

                    imageView.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                    });
                }

                //Order Important!

                setRestaurantFields();
                getMenuItems();
                addMenuItem();


            }
        }
    }


    private void setListeners(){
        restaurant.observe(this, new Observer<RestaurantDTO>() {
            @Override
            public void onChanged(RestaurantDTO newRestaurant) {
                checkAccount();
            }
        });

        menuItems.observe(this, new Observer<List<MenuItemDTO>>() {
            @Override
            public void onChanged(List<MenuItemDTO> menuItems) {
                showMenuItems();
            }
        });
    }

    private void getRestaurant() {
        RestaurantRepository.getInstance().getRestaurant(new BaseCallback<RestaurantDTO>() {
            @Override
            public void onSuccess(RestaurantDTO result) {
                restaurant.setValue(result);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(RestaurantHomePageActivity.this, "Restaurant information cannot fetched", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRestaurantFields() {
        TextView restaurantNameText = findViewById(R.id.restaurant_name);
        restaurantNameText.setText(restaurant.getValue().getRestaurantName());

        TextView addressText = findViewById(R.id.address);
        BusAddressDTO address =  restaurant.getValue().getBusAddress();
        if(address != null){
            addressText.setText(address.addressToString());
        }


        TextView resRatingText = findViewById(R.id.restaurant_rate);
        resRatingText.setText(String.valueOf(restaurant.getValue().getRating()));

        TextView ratingText = findViewById(R.id.rate);
        ratingText.setText(String.valueOf(restaurant.getValue().getRating()));


        LocalDate now  = LocalDate.now();
        DayOfWeek day = now.getDayOfWeek();

        OpeningHourDTO openingHour = restaurant.getValue().getOpeningHours().stream().filter(opH -> opH.getDayOfWeek().equals(day)).collect(Collectors.toList()).getFirst();

        TextView time = findViewById(R.id.time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = openingHour.getOpenTime().format(formatter) + " - " + openingHour.getCloseTime().format(formatter);
        time.setText(formattedTime);

        TextView statusText = findViewById(R.id.open_status);

        LocalTime open = openingHour.getOpenTime();
        LocalTime close = openingHour.getCloseTime();

        LocalTime now_time = LocalTime.of(23,12); //LocalTime.now();

        if (!now_time.isBefore(open) && !now_time.isAfter(close)) {
            statusText.setText("OPEN");
            statusText.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
        } else {
            statusText.setText("CLOSE");
            statusText.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));
        }

        setImage(restaurant.getValue().getImage());
    }

    private void getMenuItems(){
        RestaurantRepository.getInstance().getMenuItems(new BaseCallback<List<MenuItemDTO>>() {
            @Override
            public void onSuccess(List<MenuItemDTO> result) {
            }

            @Override
            public void onError(Throwable t) {
                Log.e("MenuItem", "Hata oluştu: ", t);
            }
        });
    }

    private void showMenuItems(){
        LayoutInflater inflater = LayoutInflater.from(this);
        GridLayout foodGrid = findViewById(R.id.food_grid);
        foodGrid.removeAllViews();

        for (MenuItemDTO menuItem : menuItems.getValue()) {
            View card = inflater.inflate(R.layout.restaurant_edit_cart, foodGrid, false);

            ImageView food_image = card.findViewById(R.id.food_image);
            TextView name = card.findViewById(R.id.food_name);
            TextView price = card.findViewById(R.id.food_price);
            TextView calorie = card.findViewById(R.id.food_calorie);
            TextView allergens = card.findViewById(R.id.food_allergens);
            if(menuItem.getImage() != null){
                food_image.setImageBitmap(Utils.getImageAsBitmap(menuItem.getImage()));
            }
            name.setText(menuItem.getName());
            price.setText(String.format(Locale.US, "$ %.2f", menuItem.getPrice()));
            calorie.setText(String.format(Locale.US, "%d cal", menuItem.getCalorie()));
            allergens.setText(menuItem.getAllergens() != null ?
                    menuItem.getAllergens().stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")) : "");


            card.setOnClickListener(v -> {
                Intent intent = new Intent(RestaurantHomePageActivity.this, EditMenuItemActivity.class);
                intent.putExtra("menuItemId", menuItem.getItemId());
                startActivity(intent);
                //finish();
            });

            foodGrid.addView(card);
        }

    }

    private void addMenuItem() {
        ImageView addMenuItemButton = findViewById(R.id.add_item_button);

        addMenuItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantHomePageActivity.this, AddMenuActivity.class);
            startActivity(intent);
        });
    }

    private void setImage(String image){
        if(image != null){
            imageView.setImageBitmap(Utils.getImageAsBitmap(image));
            ImageView cameraIcon = findViewById(R.id.camera_icon);
            cameraIcon.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageView.setImageBitmap(bitmap); // Seçilen resmi göster

                    ImageView cameraIcon = findViewById(R.id.camera_icon);
                    cameraIcon.setVisibility(View.GONE); // İkonu gizle

                    // Base64'e çevir
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byte[] imageBytes = stream.toByteArray();
                    base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    RestaurantRepository.getInstance().updateProfilePhoto(base64Image, new BaseCallback<RestaurantDTO>() {
                        @Override
                        public void onSuccess(RestaurantDTO result) {
                            Toast.makeText(RestaurantHomePageActivity.this, "Image uploaded.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image load failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}