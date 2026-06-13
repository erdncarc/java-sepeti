package com.example.javasepeti.customer;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.UpdateCusAddressRequest;
import com.example.core.model.Location;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import com.example.core.dto.general.AddressDTO;
import com.example.javasepeti.fragment.MapAddressPickerFragment;

public class EditAddressActivity extends BaseCustomerActivity {

    private EditText titleEditText, detailsEditText, cityEditText, townEditText, streetEditText, apartmentEditText, numberEditText;
    private Long addressId;
    private CusAddressResponseDTO address;

    private LatLng initialLatLng;

    private MapAddressPickerFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_address);

        titleEditText = findViewById(R.id.title);
        detailsEditText = findViewById(R.id.address);
        cityEditText = findViewById(R.id.city);
        townEditText = findViewById(R.id.town);
        streetEditText = findViewById(R.id.street);
        apartmentEditText = findViewById(R.id.apartment);
        numberEditText = findViewById(R.id.number);

        addressId = getIntent().getLongExtra("addressId", -1);
        address = CustomerStore.getAddress(addressId);
        setFields();

        mapFragment = new MapAddressPickerFragment();
        mapFragment.setInitialAddress(address);

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

        findViewById(R.id.delete).setOnClickListener(v -> deleteAddress());
        findViewById(R.id.update).setOnClickListener(v -> updateAddress());
    }

    private void fillFieldsFromDTO(AddressDTO dto) {
        if (dto.getStreet() != null) streetEditText.setText(dto.getStreet());
        if (dto.getNumber() != null) numberEditText.setText(String.valueOf(dto.getNumber()));
        if (dto.getTown() != null) townEditText.setText(dto.getTown());
        if (dto.getCity() != null) cityEditText.setText(dto.getCity());
        if (dto.getApartment() != null) apartmentEditText.setText(dto.getApartment());
        if (dto.getDetails() != null) detailsEditText.setText(dto.getDetails());
    }

    private void deleteAddress() {
        CustomerRepository.getInstance().deleteAddress(addressId, new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                Toast.makeText(EditAddressActivity.this, "Address silindi.", Toast.LENGTH_SHORT).show();
                goToSavedAddresses();
            }


            @Override
            public void onError(Throwable t) {
                Toast.makeText(EditAddressActivity.this, "Silme hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void setFields(){
        titleEditText.setText(address.getTitle());
        detailsEditText.setText(address.getDetails());
        cityEditText.setText(address.getCity());
        townEditText.setText(address.getTown());
        streetEditText.setText(address.getStreet());
        apartmentEditText.setText(address.getApartment());
        numberEditText.setText(String.valueOf(address.getNumber()));
    }

    private void updateAddress() {
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
                TextUtils.isEmpty(numberText)) {
            Toast.makeText(this, "Lütfen street ve town hariç tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        int number;

        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Geçerli bir sayı giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }


        UpdateCusAddressRequest request = new UpdateCusAddressRequest();
        request.setAddressId(addressId);
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

        CustomerRepository.getInstance().updateAddress(request, new BaseCallback<CusAddressResponseDTO>() {
            @Override
            public void onSuccess(CusAddressResponseDTO result) {
                Toast.makeText(EditAddressActivity.this, "Address güncellendi.", Toast.LENGTH_SHORT).show();
                goToSavedAddresses();
            }



            @Override
            public void onError(Throwable t) {
                Toast.makeText(EditAddressActivity.this, "Güncelleme hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void goToSavedAddresses() {
        //Intent intent = new Intent(this, SavedAddressesActivity.class);
        //startActivity(intent);
        finish();
    }
}
