package com.example.javasepeti.customer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.general.CartItemDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

import java.util.ArrayList;
import java.util.List;

public class BasketDialogFragment extends DialogFragment {

    private LinearLayout cartCardsContainer;
    private TextView subtotalText;
    private TextView totalText;
    private Button checkoutButton;
    private List<CartItemDTO> cartItems = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        View view = getLayoutInflater().inflate(R.layout.basket_tab, null);
        initViews(view);

        CustomerStore.cart.observe(this, new Observer<CartResponseDTO>() {
            @Override
            public void onChanged(CartResponseDTO cartResponseDTO) {
                updateCartView();
            }
        });
        updateCartView();

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.5f;
            window.setAttributes(params);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.TOP | Gravity.END;
            window.setAttributes(params);
        }
    }

    private void initViews(View view) {
        cartCardsContainer = view.findViewById(R.id.cart_cards);
        subtotalText = view.findViewById(R.id.subtotal);
        totalText = view.findViewById(R.id.total);
        checkoutButton = view.findViewById(R.id.id4);
        checkoutButton.setOnClickListener(v -> {
            dismiss();
            requireActivity().startActivity(new Intent(requireContext(), BasketConfirmActivity.class));
        });
    }

    private void updateCartView(){
        if(CustomerStore.cart.isInitialized() && CustomerStore.cart.getValue() != null){
            cartItems.clear();
            cartItems.addAll(CustomerStore.cart.getValue().getCartItems());
            displayCartItems();
            recalculateTotals();
        }else{
            CustomerRepository.getInstance().getCart(new BaseCallback<CartResponseDTO>() {
                @Override
                public void onSuccess(CartResponseDTO cart) {
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireContext(), "Sepet yüklenemedi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void displayCartItems() {
        if (!isAdded()) return; // Fragment attach edilmediyse çık

        cartCardsContainer.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for (CartItemDTO item : cartItems) {
            View card = inflater.inflate(R.layout.basket_card, cartCardsContainer, false);
            bindCartItem(card, item);
            cartCardsContainer.addView(card);
        }
    }

    private void bindCartItem(View card, CartItemDTO item) {
        TextView nameText = card.findViewById(R.id.name);
        TextView priceText = card.findViewById(R.id.price);
        TextView quantityText = card.findViewById(R.id.quantity);
        ImageButton plusButton = card.findViewById(R.id.plus);
        ImageButton minusButton = card.findViewById(R.id.minus);

        nameText.setText(item.getMenuItem().getName());
        priceText.setText(String.format("$ %.2f", item.getMenuItem().getPrice().doubleValue()));
        quantityText.setText(String.valueOf(item.getQuantity()));

        plusButton.setOnClickListener(v ->
                CustomerRepository.getInstance().updateCartItem(
                        item.getCartItemId(),
                        item.getQuantity() + 1,
                        new BaseCallback<CartResponseDTO>() {
                            @Override
                            public void onSuccess(CartResponseDTO cart) {
                                item.setQuantity(item.getQuantity() + 1);
                                quantityText.setText(String.valueOf(item.getQuantity()));
                                recalculateTotals();
                            }
                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Miktar güncellenemedi", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );

        minusButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                CustomerRepository.getInstance().updateCartItem(
                        item.getCartItemId(),
                        item.getQuantity() - 1,
                        new BaseCallback<CartResponseDTO>() {
                            @Override
                            public void onSuccess(CartResponseDTO cart) {
                                item.setQuantity(item.getQuantity() - 1);
                                quantityText.setText(String.valueOf(item.getQuantity()));
                                recalculateTotals();
                            }
                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Miktar güncellenemedi", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } else {
                CustomerRepository.getInstance().removeItemFromCart(
                        item.getCartItemId(),
                        new BaseCallback<CartResponseDTO>() {
                            @Override
                            public void onSuccess(CartResponseDTO cart) {
                                cartItems.remove(item);
                                displayCartItems();
                                recalculateTotals();
                            }
                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Öğe silinemedi", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    private void recalculateTotals() {
        double subtotal = 0;
        for (CartItemDTO item : cartItems) {
            subtotal += item.getMenuItem().getPrice().doubleValue() * item.getQuantity();
        }
        double delivery = cartItems.isEmpty() ? 0.00:3.00;

        double total = subtotal + delivery;

        subtotalText.setText(String.format("$ %.2f", subtotal));
        totalText.setText(String.format("$ %.2f", total));
    }
}
