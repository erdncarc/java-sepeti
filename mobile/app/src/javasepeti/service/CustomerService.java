package com.example.javasepeti.service;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.core.dto.customer.CustomerDTO;
import com.example.core.network.BaseCallback;
import com.example.core.repository.CustomerRepository;
import com.example.core.repository.PublicRepository;
import com.example.core.store.CustomerStore;

import java.util.List;

public class CustomerService {

    AppCompatActivity activity;

    public CustomerService(AppCompatActivity activity) {
        this.activity = activity;
    }


}
