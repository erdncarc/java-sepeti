package com.example.javasepeti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.enums.UserRole;
import com.example.javasepeti.model.Admin;
import com.example.javasepeti.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    PasswordEncoder passwordEncoder;

    AdminService adminService;

    @Autowired
    public AdminController(PasswordEncoder passwordEncoder, AdminService adminService) {
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
    }

    @PostMapping("/add_admin")
    public ResponseEntity<Admin> createAdminAccount(@RequestBody RegisterDTO registerRequest) {
        if(registerRequest.getRole() != UserRole.ADMIN){
            throw new RuntimeException("Other than ADMIN role is invalid");
        }
        return ResponseEntity.ok(adminService.createAdminAccount(registerRequest));
        
    }

}
