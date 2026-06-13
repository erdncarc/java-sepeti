package com.example.javasepeti.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.model.Admin;
import com.example.javasepeti.repository.AdminRepository;

@Service
public class AdminService extends UserService<Admin> {

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        super(adminRepository, passwordEncoder, modelMapper);
    }

    public Admin createAdminAccount(RegisterDTO registerRequest){
        return this.add(new Admin(), registerRequest);
    }
} 