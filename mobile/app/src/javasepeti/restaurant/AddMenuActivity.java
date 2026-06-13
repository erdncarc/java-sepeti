package com.example.javasepeti.restaurant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.core.dto.AddMenuItemRequest;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.enums.Allergen;
import com.example.core.enums.Label;
import com.example.core.enums.MealCategory;
import com.example.core.network.BaseCallback;
import com.example.core.repository.RestaurantRepository;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddMenuActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView imageView;
    private String base64Image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menu);


        TextView addButton = findViewById(R.id.add);
        addButton.setOnClickListener(view -> submitMenuItem());


        imageView = findViewById(R.id.add_image);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });
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

        AddMenuItemRequest request = new AddMenuItemRequest();
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

        RestaurantRepository.getInstance().addMenuItem(request, new BaseCallback<MenuItemDTO>() {
            @Override
            public void onSuccess(MenuItemDTO result) {
                finish();
            }

            @Override
            public void onError(Throwable t) {

            }
        });


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

}
