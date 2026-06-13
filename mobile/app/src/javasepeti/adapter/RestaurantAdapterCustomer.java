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

import com.example.core.Utils;
import com.example.core.dto.general.RestaurantInfoDTO;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;
import com.example.javasepeti.customer.ShowRestaurantActivity;
import com.example.javasepeti.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantAdapterCustomer extends RecyclerView.Adapter<RestaurantAdapterCustomer.ViewHolder>
        implements SearchFragment.UpdatableAdapter<RestaurantInfoDTO> {

    private final Context context;
    private final List<RestaurantInfoDTO> restaurantList = new ArrayList<>();

    public RestaurantAdapterCustomer(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantAdapterCustomer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_card_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapterCustomer.ViewHolder holder, int position) {
        RestaurantInfoDTO restaurant = restaurantList.get(position);

        holder.nameTextView.setText(restaurant.getRestaurantName() != null ? restaurant.getRestaurantName() : "Unnamed");

        if (restaurant.getRating() != null) {
            holder.starTextView.setText(String.format(Locale.US, "%.1f", restaurant.getRating().total()));
        } else {
            holder.starTextView.setText("0.0");
        }

        if (restaurant.getDistance() != null) {
            holder.distanceTextView.setText(String.format(Locale.US, "%.1f km", restaurant.getDistance()));
        } else {
            holder.distanceTextView.setText("-");
        }

        holder.stateTextView.setText(restaurant.isOpen() ? "Open" : "Closed");
        holder.stateTextView.setTextColor(
                context.getResources().getColor(restaurant.isOpen() ? R.color.green : R.color.gray)
        );

        if(restaurant.getImage() != null){
            holder.imageRestaurant.setImageBitmap(Utils.getImageAsBitmap(restaurant.getImage()));
        }

        holder.imageFavorite.setOnClickListener(view -> {
            //CustomerRepository.getInstance().addFavorite()
        });

        // TODO: Image yükleme yapılacaksa Glide/Picasso ile ekleyebilirsin
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

        TextView nameTextView, starTextView, distanceTextView, stateTextView;
        ImageView imageRestaurant, imageFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRestaurant = itemView.findViewById(R.id.pp_restaurant);
            nameTextView = itemView.findViewById(R.id.text_restaurant_name);
            starTextView = itemView.findViewById(R.id.text_star);
            distanceTextView = itemView.findViewById(R.id.text_distance);
            stateTextView = itemView.findViewById(R.id.text_state);
            imageFavorite = itemView.findViewById(R.id.image_favorite);
        }
    }
}
