package com.example.javasepeti.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;

public class SignUpCourier2 extends BaseActivity {

    EditText phoneField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_courier_2);

        phoneField = findViewById(R.id.phone_number);

        findViewById(R.id.next_button).setOnClickListener(v -> {
            update();
        });
    }


    private void update(){
        /*
        Courier courier = CourierStore.courier.getValue();
        courier.setPhone(phoneField.getText().toString().trim());
        CourierRepository.getInstance().updateProfile(courier, new BaseCallback<Courier>() {
            @Override
            public void onSuccess(Courier result) {
                CourierStore.courier.setValue(result);
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Update Courier Profile Failed: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Profile info did not updated." + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        */

    }


    private void nextStep(){
        Intent intent = new Intent(SignUpCourier2.this, SignUpCourier3.class);
        startActivity(intent);
    }
}
