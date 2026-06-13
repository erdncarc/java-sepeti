package com.example.javasepeti.customer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.core.store.CustomerStore;
import com.example.javasepeti.BaseActivity;
import com.example.javasepeti.R;

public class LpActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lp_details);

        TextView lp =  findViewById(R.id.lp);
        lp.setText(String.valueOf(CustomerStore.customer.getValue().getLoyPoint()));
    }
}
