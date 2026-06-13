package com.example.javasepeti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.javasepeti.R;
import com.example.javasepeti.customer.ShowRestaurantActivity;
import com.example.javasepeti.fragment.SearchFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantAdapterAnonymous extends RecyclerView.Adapter<RestaurantAdapterAnonymous.ViewHolder>
        implements SearchFragment.UpdatableAdapter<RestaurantInfoDTO> {

    private final List<RestaurantInfoDTO> restaurantList = new ArrayList<>();
    private final Context context;

    public RestaurantAdapterAnonymous(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantAdapterAnonymous.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card_anonymous, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapterAnonymous.ViewHolder holder, int position) {
        RestaurantInfoDTO restaurant = restaurantList.get(position);

        holder.name.setText(restaurant.getRestaurantName());
        BigDecimal rating = restaurant.getRating() != null ? restaurant.getRating().total() : BigDecimal.ZERO;
        holder.star.setText(String.format(Locale.US, "%.1f", rating.doubleValue()));
        holder.state.setText(restaurant.isOpen() ? "Open" : "Closed");

        // İstersen resim yükleme kütüphanesi ile image_restaurant'ı da güncelleyebilirsin (Glide/Picasso).

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowRestaurantActivity.class);
            intent.putExtra("restaurant_id", restaurant.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public void updateData(List<RestaurantInfoDTO> items) {
        restaurantList.clear();
        restaurantList.addAll(items);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, star, state;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_restaurant_name);
            star = itemView.findViewById(R.id.text_star);
            state = itemView.findViewById(R.id.text_state);
            image = itemView.findViewById(R.id.image_restaurant);
        }
    }
}
