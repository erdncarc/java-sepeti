package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.repository.CustomerRepository;
import com.example.core.network.BaseCallback;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

import java.util.List;

public class PaymentsInformationActivity extends BaseCustomerActivity {

    private LinearLayout cardListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments_information);

        cardListLayout = findViewById(R.id.card_list);
        TextView addButton = findViewById(R.id.add);

        CustomerStore.cards.observe(this, new Observer<List<CardResponseDTO>>() {
            @Override
            public void onChanged(List<CardResponseDTO> cardResponseDTOS) {
                loadCards();
            }
        });

        loadCards();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentsInformationActivity.this, AddPaymentActivity.class);
            startActivity(intent);
        });
    }


    private void showCards(){
        LayoutInflater inflater = LayoutInflater.from(PaymentsInformationActivity.this);
        cardListLayout.removeAllViews();

        for (CardResponseDTO card : CustomerStore.cards.getValue()) {
            View cardView = inflater.inflate(R.layout.mastercard, cardListLayout, false);

            TextView cardNameText = cardView.findViewById(R.id.card_name);
            TextView cardNumberText = cardView.findViewById(R.id.card_number);

            cardNameText.setText(card.getCardName());
            cardNumberText.setText(card.getCardNum());

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(PaymentsInformationActivity.this, EditPaymentActivity.class);
                intent.putExtra("cardId", card.getCardId());
                startActivity(intent);
            });

            cardListLayout.addView(cardView);
        }
    }

    private void loadCards() {
        if(CustomerStore.cards.getValue() != null){
            showCards();
        }else{
            CustomerRepository.getInstance().getCards(new BaseCallback<List<CardResponseDTO>>() {
                @Override
                public void onSuccess(List<CardResponseDTO> cards) {
                }
                @Override
                public void onError(Throwable t) {
                    Toast.makeText(PaymentsInformationActivity.this, "Kartlar yüklenemedi.", Toast.LENGTH_SHORT).show();
                }

            });
        }

    }
}
