package com.example.javasepeti.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.dto.customer.AddCardRequest;
import com.example.core.dto.customer.CardResponseDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.javasepeti.R;

public class AddPaymentActivity extends BaseCustomerActivity {

    private EditText cardNumberEditText;
    private EditText expiryDateEditText;
    private EditText cvcEditText;
    private EditText cardHolderEditText;
    private EditText cardNameEditText;
    private TextView addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_payments);

        cardNumberEditText = findViewById(R.id.card_number);
        expiryDateEditText = findViewById(R.id.date);
        cvcEditText = findViewById(R.id.cvc);
        cardHolderEditText = findViewById(R.id.user_name);
        cardNameEditText = findViewById(R.id.card_name);
        addButton = findViewById(R.id.add);

        addButton.setOnClickListener(v -> handleAddCard());
    }

    private void handleAddCard() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String cvc = cvcEditText.getText().toString().trim();
        String cardHolder = cardHolderEditText.getText().toString().trim();
        String cardName = cardNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(cardNumber) ||
                TextUtils.isEmpty(expiryDate) ||
                TextUtils.isEmpty(cvc) ||
                TextUtils.isEmpty(cardHolder) ||
                TextUtils.isEmpty(cardName)) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format kontrolleri
        if (!cardNumber.matches("\\d{16}")) {
            Toast.makeText(this, "Kart numarası 16 haneli olmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cvc.matches("\\d{3}")) {
            Toast.makeText(this, "CVC 3 haneli olmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            Toast.makeText(this, "Geçerlilik tarihi MM/YY formatında olmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        AddCardRequest request = new AddCardRequest();
        request.setCardNum(cardNumber);
        request.setExpiryDate(expiryDate);
        request.setCvc(cvc);
        request.setCardHolder(cardHolder);
        request.setCardName(cardName);

        CustomerRepository.getInstance().addCard(request, new BaseCallback<CardResponseDTO>() {
            @Override
            public void onSuccess(CardResponseDTO result) {
                Toast.makeText(AddPaymentActivity.this, "Kart eklendi.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddPaymentActivity.this, PaymentsInformationActivity.class));
                finish();
            }


            @Override
            public void onError(Throwable t) {
                Toast.makeText(AddPaymentActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}
