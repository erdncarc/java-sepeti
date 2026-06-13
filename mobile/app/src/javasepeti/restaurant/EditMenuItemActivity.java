package com.example.javasepeti.restaurant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.core.Utils;
import com.example.core.dto.AddMenuItemRequest;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.dto.restaurant.UpdateMenuItemRequest;
import com.example.core.enums.Allergen;
import com.example.core.enums.Label;
import com.example.core.enums.MealCategory;
import com.example.core.network.BaseCallback;
import com.example.core.repository.RestaurantRepository;
import com.example.core.store.RestaurantStore;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditMenuItemActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView imageView;
    private String base64Image = null;
    MenuItemDTO menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu);


        imageView = findViewById(R.id.edit_image);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        Intent intent = getIntent();
        Long menuItemId  = intent.getLongExtra("menuItemId",-1);
        if(menuItemId != -1){
            menuItem = RestaurantStore.menuItems.getValue().stream().filter(menuItemDTO -> menuItemDTO.getItemId() == menuItemId).collect(Collectors.toList()).getFirst();
            setFields();
        }

        findViewById(R.id.update).setOnClickListener(view -> {
            submitMenuItem();
        });

        findViewById(R.id.delete).setOnClickListener(view -> {
            setClickListenerForDelete();
        });

    }

    private void setFields(){
        EditText nameField = findViewById(R.id.name);
        nameField.setText(menuItem.getName());

        EditText priceField = findViewById(R.id.price);
        priceField.setText(String.valueOf(menuItem.getPrice().doubleValue()));

        EditText descField = findViewById(R.id.description);
        descField.setText(menuItem.getDescription());

        EditText calorieField = findViewById(R.id.calorie);
        calorieField.setText(String.valueOf(menuItem.getCalorie()));

        EditText proteinField = findViewById(R.id.protein);
        proteinField.setText(String.valueOf(menuItem.getProtein()));

        EditText fatField = findViewById(R.id.fat);
        fatField.setText(String.valueOf(menuItem.getFat()));

        EditText carbField = findViewById(R.id.carb);
        carbField.setText(String.valueOf(menuItem.getCarb()));

        CheckBox vegan = findViewById(R.id.vegan);
        CheckBox vegetarian = findViewById(R.id.vegetarian);
        vegan.setActivated(false);
        vegetarian.setActivated(false);

        for (Label lbl: menuItem.getLabels()){
            if(lbl == Label.VEGAN){
                vegan.setChecked(true);
            }
            if(lbl == Label.VEGETARIAN){
                vegetarian.setChecked(true);
            }
        }


        // ✅ Alerjenler
        int[] allergenIds = { R.id.a, R.id.b, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.h,
                R.id.l, R.id.m, R.id.n, R.id.o, R.id.p, R.id.r };
        Allergen[] allergenEnums = Allergen.values();
        for (int i = 0; i < allergenIds.length && i < allergenEnums.length; i++) {
            CheckBox cb = findViewById(allergenIds[i]);
            Allergen curAllergen = allergenEnums[i];
            boolean isExist = !menuItem.getAllergens().stream().filter(allergen ->  allergen == curAllergen).collect(Collectors.toList()).isEmpty();
            cb.setChecked(isExist);
        }

        setImage(menuItem.getImage());
    }


    private void submitMenuItem() {
        EditText nameField = findViewById(R.id.name);
        EditText priceField = findViewById(R.id.price);
        EditText descField = findViewById(R.id.description);

        EditText calorieField = findViewById(R.id.calorie);
        EditText proteinField = findViewById(R.id.protein);
        EditText fatField = findViewById(R.id.fat);
        EditText carbField = findViewById(R.id.carb);

        String name = nameField.getText().toString().trim();
        String priceText = priceField.getText().toString().trim();
        String desc = descField.getText().toString().trim();

        String calText = calorieField.getText().toString().trim();
        String proText = proteinField.getText().toString().trim();
        String fatText = fatField.getText().toString().trim();
        String carbText = carbField.getText().toString().trim();

        // ✅ Validasyonlar
        if (name.isEmpty()) {
            nameField.setError("Name is required.");
            return;
        }

        if (priceText.isEmpty()) {
            priceField.setError("Price is required.");
            return;
        }

        if (!priceText.matches("\\d+(\\.\\d+)?")) {
            priceField.setError("Enter a valid price (e.g. 9.99)");
            return;
        }

        if (calText.isEmpty() || proText.isEmpty() || fatText.isEmpty() || carbText.isEmpty()) {
            Toast.makeText(this, "Please fill in all nutrition fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateMenuItemRequest request = new UpdateMenuItemRequest();
        request.setItemId(menuItem.getItemId());
        request.setName(name);
        request.setDescription(desc);
        request.setPrice(new BigDecimal(priceText));

        request.setCalorie(Integer.parseInt(calText));
        request.setProtein(Integer.parseInt(proText));
        request.setFat(Integer.parseInt(fatText));
        request.setCarb(Integer.parseInt(carbText));

        // ✅ Vegan / Vegetarian etiketleri
        List<Label> labels = new ArrayList<>();
        if (((CheckBox) findViewById(R.id.vegan)).isChecked()) labels.add(Label.VEGAN);
        if (((CheckBox) findViewById(R.id.vegetarian)).isChecked()) labels.add(Label.VEGETARIAN);
        request.setLabels(labels);

        // ✅ Alerjenler
        int[] allergenIds = { R.id.a, R.id.b, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.h,
                R.id.l, R.id.m, R.id.n, R.id.o, R.id.p, R.id.r };
        List<Allergen> allergens = new ArrayList<>();
        Allergen[] allergenEnums = Allergen.values();
        for (int i = 0; i < allergenIds.length && i < allergenEnums.length; i++) {
            CheckBox cb = findViewById(allergenIds[i]);
            if (cb.isChecked()) {
                allergens.add(allergenEnums[i]);
            }
        }
        request.setAllergens(allergens);

        //Fiz Later
        List<MealCategory> categories = new ArrayList<>();
        //categories.add(MealCategory.MEAT); // Dinamikse UI'dan alınmalı
        request.setCategories(categories);

        request.setImage(base64Image);


        // ✅ API çağrısı

        RestaurantRepository.getInstance().updateMenuItem(request, new BaseCallback<MenuItemDTO>() {
            @Override
            public void onSuccess(MenuItemDTO result) {
                Toast.makeText(EditMenuItemActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable t) {

            }
        });


    }

    private void setImage(String image){
        if(image != null){
            imageView.setImageBitmap(Utils.getImageAsBitmap(image));
            ImageView cameraIcon = findViewById(R.id.camera_icon);
            cameraIcon.setVisibility(View.GONE);
        }
    }


    private int parseIntSafe(int editTextId) {
        try {
            String val = ((EditText) findViewById(editTextId)).getText().toString().trim();
            return val.isEmpty() ? 0 : Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
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

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image load failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void setClickListenerForDelete() {

        TextView deleteTextView = findViewById(R.id.delete);
        deleteTextView.setOnClickListener(v -> {
            Long menuItemId = menuItem.getItemId();

            RestaurantRepository.getInstance().deleteMenuItem(menuItemId, new BaseCallback<MessageResponseDTO>() {
                @Override
                public void onSuccess(MessageResponseDTO result) {
                    Toast.makeText(EditMenuItemActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Throwable t) {
                    //Toast.makeText(EditMenuItemActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteMenuItem", "Error: ", t);
                }
            });
        });

    }
}