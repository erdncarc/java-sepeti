package com.example.javasepeti.auth;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.dto.customer.AddCusAddressRequest;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.customer.CustomerDTO;
import com.example.core.model.Location;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.javasepeti.BaseActivity;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;
import com.example.javasepeti.customer.CustomerMainActivity;
import com.example.javasepeti.service.CustomerService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;

public class SignUpCustomer3 extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;

    private EditText addressField, cityField, townField, streetField, apartmentField, numberField, titleField;
    private Marker marker;

    private CustomerService customerService = new CustomerService(SignUpCustomer3.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_customer_3);

        // EditText alanlarını eşle
        addressField = findViewById(R.id.address);
        cityField = findViewById(R.id.city);
        townField = findViewById(R.id.town);
        streetField = findViewById(R.id.street);
        apartmentField = findViewById(R.id.apartment);
        numberField = findViewById(R.id.number);
        titleField = findViewById(R.id.title); // yeni alan

        // Geocoder başlat
        geocoder = new Geocoder(this, Locale.getDefault());

        // Haritayı yükle
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        TextView nextButton = findViewById(R.id.next_button);


        nextButton.setOnClickListener(v -> {

            double latitude = (marker != null) ? marker.getPosition().latitude : 0;
            double longitude = (marker != null) ? marker.getPosition().longitude : 0;

            AddCusAddressRequest req = new AddCusAddressRequest();

            Location location = new Location();
            location.setLatitude(BigDecimal.valueOf(latitude));
            location.setLongitude(BigDecimal.valueOf(longitude));

            req.setLocation(location);
            req.setCity(cityField.getText().toString().trim());
            req.setTown(townField.getText().toString().trim());
            req.setStreet(streetField.getText().toString().trim());
            req.setApartment(apartmentField.getText().toString().trim());
            req.setNumber(Integer.valueOf(numberField.getText().toString().trim()));
            req.setTitle(titleField.getText().toString().trim());
            req.setDetails(addressField.getText().toString().trim());

            // Adres boşluk kontrolü yapılabilir
            if (req.getCity().isEmpty() || req.getTown().isEmpty() || req.getStreet().isEmpty() || req.getTitle().isEmpty() || req.getApartment().isEmpty()|| req.getNumber().toString().isEmpty()) {
                Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            addAddress(req); // API'ye gönderme fonksiyonu
        });



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Varsayılan konum: Ankara
        LatLng defaultLocation = new LatLng(39.9208, 32.8541);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Harita tıklanınca marker koy ve adresi al
        mMap.setOnMapClickListener(latLng -> {
            if (marker != null) marker.remove();

            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            getAddressFromLocation(latLng);
        });
    }

    private void getAddressFromLocation(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                addressField.setText(address.getAddressLine(0));
                cityField.setText(address.getAdminArea()); // İl
                townField.setText(address.getSubAdminArea()); // İlçe
                streetField.setText(address.getThoroughfare()); // Cadde/sokak

                Log.d("GEO", "Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);
            }

        } catch (IOException e) {
            Toast.makeText(this, "Address could not be retrieved", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void addAddress(AddCusAddressRequest address){
        CustomerRepository.getInstance().addAddress(address, new BaseCallback<CusAddressResponseDTO>() {
            @Override
            public void onSuccess(CusAddressResponseDTO result) {
                completeRegister();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Address Add Operation Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void completeRegister(){
        CustomerRepository.getInstance().completeRegister(new BaseCallback<CustomerDTO>() {
            @Override
            public void onSuccess(CustomerDTO result) {
                nextStep();
            }

            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void nextStep(){
        Intent intent = new Intent(SignUpCustomer3.this, CustomerMainActivity.class);
        startActivity(intent);
    }

}




