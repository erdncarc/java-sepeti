package com.example.javasepeti.customer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.core.dto.MakeAnOrderRequest;
import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.dto.customer.CartResponseDTO;
import com.example.core.dto.customer.CusAddressResponseDTO;
import com.example.core.dto.general.CartItemDTO;
import com.example.core.dto.general.OrderDTO;
import com.example.core.enums.PaymentMethod;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

public class BasketConfirmActivity extends BaseCustomerActivity {

    private LinearLayout basketCardsContainer;
    private TextView subtotalText;
    private TextView discountText;
    private TextView totalText;
    private TextView addressTitle;
    private TextView addressSubtitle;
    private TextView cardName;
    private TextView cardNumber;
    private Button confirmButton;
    private final List<CartItemDTO> cartItems = new ArrayList<>();

    private Long cardId;
    private Long addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_confirm);
        initViews();
        loadCartItems();
        setupAddressSelector();
        setupCardSelector();
        setupConfirmButton();
    }

    private void initViews() {
        basketCardsContainer = findViewById(R.id.basket_cards);
        subtotalText = findViewById(R.id.subtotal);
        discountText = findViewById(R.id.discount);
        totalText = findViewById(R.id.total);
        addressTitle = findViewById(R.id.title);
        addressSubtitle = findViewById(R.id.subtitle);
        cardName = findViewById(R.id.card_name);
        cardNumber = findViewById(R.id.card_number);
        confirmButton = findViewById(R.id.confirm_basket);
    }

    private void loadCartItems() {



        CustomerRepository.getInstance().getCart(new BaseCallback<CartResponseDTO>() {
            @Override
            public void onSuccess(CartResponseDTO cart) {
                cartItems.clear();
                cartItems.addAll(cart.getCartItems());
                displayCartItems();
                updateSummary();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(BasketConfirmActivity.this, "Sepet yüklenemedi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCartItems() {
        basketCardsContainer.removeAllViews();
        for (CartItemDTO item : cartItems) {
            View card = getLayoutInflater().inflate(R.layout.basket_card, basketCardsContainer, false);
            bindCartItem(card, item);
            basketCardsContainer.addView(card);
        }
    }

    private void bindCartItem(View card, CartItemDTO item) {
        TextView nameView = card.findViewById(R.id.name);
        TextView priceView = card.findViewById(R.id.price);
        TextView qtyView = card.findViewById(R.id.quantity);
        ImageButton plus = card.findViewById(R.id.plus);
        ImageButton minus = card.findViewById(R.id.minus);

        nameView.setText(item.getMenuItem().getName());
        priceView.setText(String.format("$ %.2f", item.getMenuItem().getPrice().doubleValue()));
        qtyView.setText(String.valueOf(item.getQuantity()));

        plus.setOnClickListener(v -> CustomerRepository.getInstance().updateCartItem(item.getCartItemId(), item.getQuantity() + 1, new BaseCallback<CartResponseDTO>() {
            @Override
            public void onSuccess(CartResponseDTO cart) {
                item.setQuantity(item.getQuantity() + 1);
                qtyView.setText(String.valueOf(item.getQuantity()));
                updateSummary();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(BasketConfirmActivity.this, "Miktar güncellenemedi", Toast.LENGTH_SHORT).show();
            }
        }));

        minus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                CustomerRepository.getInstance().updateCartItem(item.getCartItemId(), item.getQuantity() - 1, new BaseCallback<CartResponseDTO>() {
                    @Override
                    public void onSuccess(CartResponseDTO cart) {
                        item.setQuantity(item.getQuantity() - 1);
                        qtyView.setText(String.valueOf(item.getQuantity()));
                        updateSummary();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(BasketConfirmActivity.this, "Miktar güncellenemedi", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // quantity == 1: remove item
                CustomerRepository.getInstance().removeItemFromCart(item.getCartItemId(), new BaseCallback<CartResponseDTO>() {
                    @Override
                    public void onSuccess(CartResponseDTO cart) {
                        cartItems.remove(item);
                        displayCartItems();
                        updateSummary();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(BasketConfirmActivity.this, "Öğe silinemedi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateSummary() {
        double subtotal = 0;
        for (CartItemDTO item : cartItems) {
            subtotal += item.getMenuItem().getPrice().doubleValue() * item.getQuantity();
        }
        double delivery = 3.00;
        double discount = 0.00;
        double total = subtotal + delivery - discount;

        subtotalText.setText(String.format("$ %.2f", subtotal));
        discountText.setText(String.format("-$ %.2f", discount));
        totalText.setText(String.format("$ %.2f", total));
    }

    private void setupAddressSelector() {
        findViewById(R.id.address).setOnClickListener(v->{
            List<CusAddressResponseDTO> addresses = CustomerStore.cusAddresses.getValue();
            SelectDialogFragment.newInstance(addresses, R.layout.address, (view, addr) -> {
                TextView t = view.findViewById(R.id.title);
                TextView s = view.findViewById(R.id.subtitle);
                t.setText(addr.getTitle());
                s.setText(addr.getStreet() + ", " + addr.getCity());
            }, addr -> {
                addressId = addr.getAddressId();

                addressTitle.setText(addr.getTitle());
                addressSubtitle.setText(addr.getStreet() + ", " + addr.getCity());
            }).show(getSupportFragmentManager(), "address_select");
        });
    }

    private void setupCardSelector() {
        findViewById(R.id.cards).setOnClickListener(v -> CustomerRepository.getInstance().getCards(new BaseCallback<List<CardResponseDTO>>() {
            @Override
            public void onSuccess(List<CardResponseDTO> cards) {
                SelectDialogFragment.newInstance(cards, R.layout.mastercard, (view, c) -> {
                    TextView tName = view.findViewById(R.id.card_name);
                    TextView tNum = view.findViewById(R.id.card_number);
                    tName.setText(c.getCardName());
                    String num = c.getCardNum();
                    tNum.setText("**** " + num.substring(num.length() - 4));
                }, c -> {
                    cardId = c.getCardId();

                    cardName.setText(c.getCardName());
                    String num = c.getCardNum();
                    cardNumber.setText("**** " + num.substring(num.length() - 4));
                }).show(getSupportFragmentManager(), "card_select");
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(BasketConfirmActivity.this, "Kart alınamadı", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void setupConfirmButton() {
        confirmButton.setOnClickListener(v -> {
            makeOrder();
        });
    }


    private void makeOrder(){
        MakeAnOrderRequest req = new MakeAnOrderRequest();
        req.setPaymentMethod(PaymentMethod.CARD);
        req.setNote("");
        req.setCardId(cardId);
        req.setAddressId(addressId);
        CustomerRepository.getInstance().makeAnOrder(req, new BaseCallback<OrderDTO>() {
            @Override
            public void onSuccess(OrderDTO result) {
                Toast.makeText(getApplicationContext(), "Sipariş onaylandı", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Sipariş onaylanmadı", Toast.LENGTH_SHORT).show();
            }
        });
    }
}