package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.core.dto.UpdateCardRequest;
import com.example.core.dto.auth.MessageResponseDTO;
import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.dto.customer.UpdateCusAddressRequest;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.store.CustomerStore;
import com.example.javasepeti.R;

public class EditPaymentActivity extends BaseCustomerActivity {

    private EditText cardNumberEdit, expiryDateEdit, cvcEdit, fullNameEdit, cardNameEdit;
    private Long cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_payments);

        cardNumberEdit = findViewById(R.id.card_number);
        expiryDateEdit = findViewById(R.id.date);
        cvcEdit = findViewById(R.id.cvc);
        fullNameEdit = findViewById(R.id.user_name);
        cardNameEdit = findViewById(R.id.card_name);

        Intent intent = getIntent();
        cardId = intent.getLongExtra("cardId", -1);
        CardResponseDTO card =  CustomerStore.getCard(cardId);

        cardNumberEdit.setText(card.getCardNum());
        expiryDateEdit.setText(card.getExpiryDate());
        fullNameEdit.setText(card.getCardHolder());
        cardNameEdit.setText(card.getCardName());

        findViewById(R.id.delete).setOnClickListener(v -> deleteCard());
        findViewById(R.id.update).setOnClickListener(v -> updateCard());
    }

    private void deleteCard() {
        CustomerRepository.getInstance().deleteCard(cardId, new BaseCallback<MessageResponseDTO>() {
            @Override
            public void onSuccess(MessageResponseDTO result) {
                Toast.makeText(EditPaymentActivity.this, "Kart silindi.", Toast.LENGTH_SHORT).show();
                goToPaymentsInformation();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(EditPaymentActivity.this, "Silme hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void updateCard() {
        String cardNum = cardNumberEdit.getText().toString().trim();
        String expiryDate = expiryDateEdit.getText().toString().trim();
        String cvc = cvcEdit.getText().toString().trim();
        String cardHolder = fullNameEdit.getText().toString().trim();
        String cardName = cardNameEdit.getText().toString().trim();

        if (cardNum.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty() || cardHolder.isEmpty() || cardName.isEmpty()) {
            Toast.makeText(this, "Tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cardNum.matches("\\d{16}")) {
            Toast.makeText(this, "Kart numarası 16 haneli olmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            Toast.makeText(this, "Tarih MM/YY formatında olmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cvc.matches("\\d{3}")) {
            Toast.makeText(this, "CVC 3 haneli olmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateCardRequest request = new UpdateCardRequest();
        request.setCardNum(cardNum);
        request.setExpiryDate(expiryDate);
        request.setCvc(cvc);
        request.setCardHolder(cardHolder);
        request.setCardName(cardName);
        request.setCardId(cardId);

        CustomerRepository.getInstance().updateCard(request, new BaseCallback<CardResponseDTO>() {
            @Override
            public void onSuccess(CardResponseDTO result) {
                Toast.makeText(EditPaymentActivity.this, "Kart güncellendi.", Toast.LENGTH_SHORT).show();
                goToPaymentsInformation();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(EditPaymentActivity.this, "Güncelleme hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void goToPaymentsInformation() {
        //Intent intent = new Intent(this, PaymentsInformationActivity.class);
        //startActivity(intent);
        finish();
    }
}
