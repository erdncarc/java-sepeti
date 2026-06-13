package com.example.javasepeti.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import lombok.Setter;

public class RestaurantListFragment extends Fragment {

    private LinearLayout container;
    @Setter
    private int layoutId = R.layout.restaurant_card_anonymous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        this.container = view.findViewById(R.id.restaurant_cards);
        return view;
    }

    public void displayRestaurantsCards(List<RestaurantInfoDTO> restaurants) {
        if (container == null || getContext() == null) return;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        container.removeAllViews();

        for (RestaurantInfoDTO restaurant : restaurants) {
            View cardView = inflater.inflate(layoutId, container, false);

            TextView name = cardView.findViewById(R.id.text_restaurant_name);
            TextView star = cardView.findViewById(R.id.text_star);

            name.setText(restaurant.getRestaurantName());

            BigDecimal rating = restaurant.getRating() != null ? restaurant.getRating().total() : BigDecimal.ZERO;
            star.setText(String.format(Locale.US, "%.1f", rating.doubleValue()));

            container.addView(cardView);
        }
    }
}
