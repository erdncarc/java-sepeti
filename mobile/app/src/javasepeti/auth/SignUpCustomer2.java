package com.example.javasepeti.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.core.dto.customer.CustomerDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.javasepeti.BaseActivity;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpCustomer2 extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage, addIcon;
    private TextView skip, next;
    private String base64Image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_customer_2); // XML dosyanın adını yaz

        profileImage = findViewById(R.id.profile_image);
        addIcon = findViewById(R.id.add_icon);
        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);

        addIcon.setOnClickListener(v -> openImagePicker());
        profileImage.setOnClickListener(v -> openImagePicker());

        skip.setOnClickListener(v -> goToNextScreen());

        next.setOnClickListener(v -> {
            if (!base64Image.isEmpty()) {
                uploadBase64Image(base64Image);
            } else {
                Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                base64Image = encodeImageToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image load error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private void uploadBase64Image(String base64Image) {

        CustomerRepository.getInstance().updateProfilePhoto(base64Image, new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                goToNextScreen();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void goToNextScreen() {
        Intent intent = new Intent(SignUpCustomer2.this, SignUpCustomer3.class); // bir sonraki activity
        startActivity(intent);
    }
}
