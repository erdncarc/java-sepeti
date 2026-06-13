package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.core.repository.PublicRepository;
import com.example.javasepeti.R;

public class GeneralSettingsActivity extends BaseCustomerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting_customer);

        TextView paymentsInfoButton = findViewById(R.id.payments_information);
        TextView savedAddressesButton = findViewById(R.id.saved_addresses);
        LinearLayout signOutButton = findViewById(R.id.sign_out);

        paymentsInfoButton.setOnClickListener(view -> {
            Intent intent = new Intent(GeneralSettingsActivity.this, PaymentsInformationActivity.class);
            startActivity(intent);
        });

        savedAddressesButton.setOnClickListener(view -> {
            Intent intent = new Intent(GeneralSettingsActivity.this, SavedAddressesActivity.class);
            startActivity(intent);
        });



        signOutButton.setOnClickListener(view -> {
            PublicRepository.getInstance().signOut();
        });


        findViewById(R.id.edit_profile).setOnClickListener(view -> {
            Intent intent = new Intent(GeneralSettingsActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.lp_details).setOnClickListener(view -> {
            Intent intent = new Intent(GeneralSettingsActivity.this, LpActivity.class);
            startActivity(intent);
        });




    }
}
