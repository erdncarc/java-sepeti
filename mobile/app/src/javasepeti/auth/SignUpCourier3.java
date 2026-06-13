package com.example.javasepeti.auth;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;
import com.example.javasepeti.courier.CourierMainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUpCourier3 extends BaseActivity implements OnMapReadyCallback {

    private EditText address, city, town, street, apartment, number, range;
    private TextView nextButton;
    private String name, email, password, phone;

    private GoogleMap mMap;
    private LatLng selectedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_courier_3);

        // EditText tanımlamaları
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        town = findViewById(R.id.town);
        street = findViewById(R.id.street);
        apartment = findViewById(R.id.apartment);
        number = findViewById(R.id.number);
        range = findViewById(R.id.range);

        nextButton = findViewById(R.id.next_button);

        // Harita fragmenti
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        nextButton.setOnClickListener(v -> {
            if (areAllFieldsValid()) {
                addAddress();
            } else {
                Toast.makeText(this, "Please fill all fields with valid values", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addAddress(){
        /*

        BusAddressRequest req = new BusAddressRequest();

        req.setApartment(apartment.getText().toString().trim());
        req.setNumber(Integer.valueOf(number.getText().toString().trim()));
        req.setCity(city.getText().toString().trim());
        req.setTown(town.getText().toString().trim());
        req.setStreet(street.getText().toString().trim());
        req.setDetails(address.getText().toString().trim());
        req.setLatitude(BigDecimal.valueOf(selectedLatLng.latitude));
        req.setLongitude(BigDecimal.valueOf(selectedLatLng.longitude));
        req.setRangeValue(Integer.parseInt(range.getText().toString().trim()));


        CourierRepository.getInstance().setAddress(req, new BaseCallback<Courier>() {
            @Override
            public void onSuccess(Courier result) {
                CourierStore.courier.setValue(result);
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Thrown an error while saving address. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
        */

    }

    private void nextStep(){
        Intent goToHome = new Intent(SignUpCourier3.this, CourierMainActivity.class);
        startActivity(goToHome);
        finish();
    }

    private boolean areAllFieldsValid() {
        String addr = address.getText().toString().trim();
        String cty = city.getText().toString().trim();
        String twn = town.getText().toString().trim();
        String strt = street.getText().toString().trim();
        String apt = apartment.getText().toString().trim();
        String num = number.getText().toString().trim();
        String rng = range.getText().toString().trim();

        if (addr.isEmpty() || cty.isEmpty() || twn.isEmpty() || strt.isEmpty() || apt.isEmpty() || num.isEmpty() || rng.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(num);
            Integer.parseInt(rng);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Seçilen Konum"));
            selectedLatLng = latLng;

            Toast.makeText(this, "Lat: " + latLng.latitude + ", Lng: " + latLng.longitude, Toast.LENGTH_SHORT).show();

            // Adres bilgilerini Geocoder ile al
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addr = addresses.get(0);

                    // Verileri EditText’lere yaz
                    address.setText(addr.getAddressLine(0)); // Tüm adres
                    city.setText(addr.getLocality()); // İl
                    town.setText(addr.getSubAdminArea()); // İlçe
                    street.setText(addr.getThoroughfare()); // Sokak
                    number.setText(addr.getSubThoroughfare()); // Bina numarası

                    // Diğer alanlar elle doldurulabilir
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Adres alınamadı!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}