package com.example.javasepeti.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.dto.customer.AddCartItemRequest;
import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.general.MenuItemDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.javasepeti.R;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.stream.Collectors;

public class ShowProductActivity extends BaseCustomerActivity {

    private MenuItemDTO item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_product);

        String itemJson = getIntent().getStringExtra("items");
        if (itemJson == null || itemJson.isEmpty()) {
            Toast.makeText(this, "Ürün verisi alınamadı.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        item = new Gson().fromJson(itemJson, MenuItemDTO.class);

        TextView foodName = findViewById(R.id.food_name);
        TextView starInfo = findViewById(R.id.text_star);
        TextView calorieInfo = findViewById(R.id.text_calorie);
        TextView allergensInfo = findViewById(R.id.text_allergens);
        TextView fatValue = findViewById(R.id.fat_value);
        TextView proteinValue = findViewById(R.id.protein_value);
        TextView carbValue = findViewById(R.id.carbs_value);
        TextView description = findViewById(R.id.food_description);
        TextView priceText = findViewById(R.id.price);
        View addToCartBar = findViewById(R.id.add_basket);

        foodName.setText(item.getName());
        starInfo.setText(String.format(Locale.US, "%.1f", item.getRating()));
        calorieInfo.setText(String.format(Locale.US, "%d", item.getCalorie()));
        String allergens = item.getAllergens() != null ?
                item.getAllergens().stream().map(Enum::name).collect(Collectors.joining(", ")) : "";
        allergensInfo.setText(allergens);
        fatValue.setText(String.format(Locale.US, "%d gr", item.getFat()));
        proteinValue.setText(String.format(Locale.US, "%d gr", item.getProtein()));
        carbValue.setText(String.format(Locale.US, "%d gr", item.getCarb()));
        description.setText(item.getDescription());
        priceText.setText(String.format(Locale.US, "$ %.2f", item.getPrice().doubleValue()));

        addToCartBar.setOnClickListener(v -> {
            AddCartItemRequest request = new AddCartItemRequest();
            request.setMenuItemId(item.getItemId());
            request.setQuantity(1);

            CustomerRepository.getInstance().addItemToCart(
                    request,
                    new BaseCallback<CartResponseDTO>() {
                        @Override
                        public void onSuccess(CartResponseDTO cart) {
                            Log.d("ADD_ITEM", "Sepete eklenen öğe sayısı: " + cart.getCartItems().size());
                            Toast.makeText(ShowProductActivity.this,
                                    "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.e("ADD_ITEM", "Sepete ekleme başarısız", t);
                            Toast.makeText(ShowProductActivity.this,
                                    "Sepete eklenemedi", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });
    }
}