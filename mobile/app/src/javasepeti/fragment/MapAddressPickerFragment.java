package com.example.javasepeti.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.core.dto.general.AddressDTO;
import com.example.core.model.Location;
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

import lombok.Getter;
import lombok.Setter;

public class MapAddressPickerFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Getter
    private LatLng selectedLatLng;

    @Setter
    private AddressDTO initialAddress;

    public interface AddressSelectedListener {
        void onAddressSelected(AddressDTO addressDTO);
    }

    private AddressSelectedListener listener;

    public void setAddressSelectedListener(AddressSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_address_picker, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);

        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (initialAddress != null && initialAddress.getLocation() != null) {
            LatLng latLng = new LatLng(
                    initialAddress.getLocation().getLatitude().doubleValue(),
                    initialAddress.getLocation().getLongitude().doubleValue()
            );
            mMap.addMarker(new MarkerOptions().position(latLng).title("Kayıtlı Konum"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            selectedLatLng = latLng;
            generateAddressDTOFromLatLng(latLng); // ilk başta da listener'a bildir
        }

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Seçilen Konum"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            selectedLatLng = latLng;
            generateAddressDTOFromLatLng(latLng);
        });
    }

    private void generateAddressDTOFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty() && listener != null) {
                Address loc = addresses.get(0);
                AddressDTO dto = new AddressDTO();

                dto.setCity(loc.getAdminArea());
                dto.setTown(loc.getLocality());
                dto.setStreet(loc.getThoroughfare());
                dto.setApartment(loc.getFeatureName());

                if (loc.getSubThoroughfare() != null) {
                    try {
                        dto.setNumber(Integer.parseInt(loc.getSubThoroughfare()));
                    } catch (NumberFormatException ignored) {}
                }

                dto.setDetails(loc.getAddressLine(0));

                Location location = new Location();
                location.setLatitude(BigDecimal.valueOf(latLng.latitude));
                location.setLongitude(BigDecimal.valueOf(latLng.longitude));
                dto.setLocation(location);

                listener.onAddressSelected(dto);
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Adres alınamadı", Toast.LENGTH_SHORT).show();
        }
    }

}
