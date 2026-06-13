package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.customer.AddCusAddressRequest;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.UpdateCusAddressRequest;
import com.example.core.dto.general.AddressDTO;
import com.example.core.model.Location;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;
import com.example.javasepeti.fragment.MapAddressPickerFragment;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;

public class AddAddressActivity extends BaseCustomerActivity {

    private EditText titleEditText, detailsEditText, cityEditText, townEditText, streetEditText, apartmentEditText, numberEditText;

    private LatLng initialLatLng;

    private MapAddressPickerFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_address);

        titleEditText = findViewById(R.id.title);
        detailsEditText = findViewById(R.id.address);
        cityEditText = findViewById(R.id.city);
        townEditText = findViewById(R.id.town);
        streetEditText = findViewById(R.id.street);
        apartmentEditText = findViewById(R.id.apartment);
        numberEditText = findViewById(R.id.number);


        mapFragment = new MapAddressPickerFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment) // `map` yerine `map_container` olmalı
                .commit();

        mapFragment.setAddressSelectedListener(addressDTO -> {
            fillFieldsFromDTO(addressDTO);
            if (addressDTO.getLocation() != null) {
                initialLatLng = new LatLng(
                        addressDTO.getLocation().getLatitude().doubleValue(),
                        addressDTO.getLocation().getLongitude().doubleValue()
                );
            }
        });

        findViewById(R.id.add).setOnClickListener(v -> addAddress());
    }

    private void fillFieldsFromDTO(AddressDTO dto) {
        if (dto.getStreet() != null) streetEditText.setText(dto.getStreet());
        if (dto.getNumber() != null) numberEditText.setText(String.valueOf(dto.getNumber()));
        if (dto.getTown() != null) townEditText.setText(dto.getTown());
        if (dto.getCity() != null) cityEditText.setText(dto.getCity());
        if (dto.getApartment() != null) apartmentEditText.setText(dto.getApartment());
        if (dto.getDetails() != null) detailsEditText.setText(dto.getDetails());
    }


    private void addAddress() {
        String title = titleEditText.getText().toString().trim();
        String details = detailsEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String town = townEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String apartment = apartmentEditText.getText().toString().trim();
        String numberText = numberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) ||
                TextUtils.isEmpty(details) ||
                TextUtils.isEmpty(city) ||
                TextUtils.isEmpty(apartment) ||
                TextUtils.isEmpty(numberText)||
                TextUtils.isEmpty(street) ||
                TextUtils.isEmpty(town)){
            Toast.makeText(this, "Please fill the blanks!", Toast.LENGTH_SHORT).show();
            return;
        }

        int number;

        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
            return;
        }


        AddCusAddressRequest request = new AddCusAddressRequest();
        request.setTitle(title);
        request.setDetails(details);
        request.setCity(city);
        request.setTown(town);
        request.setStreet(street);
        request.setApartment(apartment);
        request.setNumber(number);

        Location location =  new Location();
        location.setLongitude(BigDecimal.valueOf(initialLatLng.longitude));
        location.setLatitude(BigDecimal.valueOf(initialLatLng.latitude));
        request.setLocation(location);

        CustomerRepository.getInstance().addAddress(request, new BaseCallback<CusAddressResponseDTO>() {
            @Override
            public void onSuccess(CusAddressResponseDTO result) {
                Toast.makeText(AddAddressActivity.this, "Address added.", Toast.LENGTH_SHORT).show();
                goToSavedAddresses();
            }



            @Override
            public void onError(Throwable t) {
                //Toast.makeText(AddAddressActivity.this, "Güncelleme hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void goToSavedAddresses() {
        //Intent intent = new Intent(this, SavedAddressesActivity.class);
        //startActivity(intent);
        finish();
    }
}
