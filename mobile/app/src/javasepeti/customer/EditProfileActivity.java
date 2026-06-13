package com.example.javasepeti.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.core.Utils;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.dto.customer.CustomerUpdateInfoRequest;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.repository.PublicRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.example.javasepeti.auth.BaseAuthActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileActivity extends BaseCustomerActivity {

    EditText full_name, phone_number,email,password;

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView imageView;
    private String base64Image = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_customer);

        full_name =  findViewById(R.id.full_name);
        phone_number =  findViewById(R.id.phone_number);
        email =  findViewById(R.id.email);
        password  = findViewById(R.id.password);


        imageView = findViewById(R.id.pp);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        setFields();

        findViewById(R.id.update).setOnClickListener(view -> {
            update();
        });

        findViewById(R.id.sign_out).setOnClickListener(view -> {
            signOut();
        });

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

    protected void setFields(){
        if(CustomerStore.customer.getValue() != null){
            full_name.setText(CustomerStore.customer.getValue().getName());
            phone_number.setText(CustomerStore.customer.getValue().getPhone());
            email.setText(CustomerStore.customer.getValue().getEmail());
            if(CustomerStore.customer.getValue().getImage() != null){
                imageView.setImageBitmap(Utils.getImageAsBitmap(CustomerStore.customer.getValue().getImage()));
            }
        }
    }


    protected void update(){
        CustomerUpdateInfoRequest req = new CustomerUpdateInfoRequest();
        req.setName(full_name.getText().toString().trim());
        req.setEmail(email.getText().toString().trim());
        req.setPhone(phone_number.getText().toString().trim());
        CustomerRepository.getInstance().updateProfileInfo(req, new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                Toast.makeText(EditProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                setFields();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });
        if(base64Image != null){
            CustomerRepository.getInstance().updateProfilePhoto(base64Image, new BaseCallback<CustomerDTO>() {
                @Override
                public void onSuccess(CustomerDTO result) {
                    Toast.makeText(EditProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    setFields();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    protected void signOut(){
        //PublicRepository.getInstance().signOut();
    }
}
