package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

import java.util.ArrayList;
import java.util.List;

public class SavedAddressesActivity extends BaseCustomerActivity {

    private LinearLayout addressListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_addresses);

        addressListLayout = findViewById(R.id.address_list);
        TextView addButton = findViewById(R.id.add);

        CustomerStore.cusAddresses.observe(this, new Observer<List<CusAddressResponseDTO>>() {
            @Override
            public void onChanged(List<CusAddressResponseDTO> cusAddressResponseDTOS) {
                loadAddresses();
            }
        });

        loadAddresses();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(SavedAddressesActivity.this, AddAddressActivity.class);
            startActivity(intent);
        });



    }

    private void showAddresses(){
        LayoutInflater inflater = LayoutInflater.from(SavedAddressesActivity.this);
        addressListLayout.removeAllViews();

        for (CusAddressResponseDTO address : CustomerStore.cusAddresses.getValue()) {
            View cardView = inflater.inflate(R.layout.address, addressListLayout, false);

            TextView titleText = cardView.findViewById(R.id.title);
            TextView subtitleText = cardView.findViewById(R.id.subtitle);

            titleText.setText(address.getTitle());
            subtitleText.setText(address.getDetails());

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(SavedAddressesActivity.this, EditAddressActivity.class);
                intent.putExtra("addressId", address.getAddressId());
                startActivity(intent);
            });

            addressListLayout.addView(cardView);
        }
    }

    private void loadAddresses() {
        if(CustomerStore.cusAddresses.getValue() != null){
            showAddresses();
        }else{
            CustomerRepository.getInstance().getAddresses(new BaseCallback<List<CusAddressResponseDTO>>() {
                @Override
                public void onSuccess(List<CusAddressResponseDTO> addresses) {

                }


                @Override
                public void onError(Throwable t) {
                    Toast.makeText(SavedAddressesActivity.this, "Kartlar yüklenemedi.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
