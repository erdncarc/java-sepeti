package com.example.javasepeti.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("isAuthenticated", authentication.isAuthenticated());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin")
    public ResponseEntity<?> adminEndpoint() {
        return ResponseEntity.ok("Admin endpoint çalışıyor!");
    }
    
    @GetMapping("/customer")
    public ResponseEntity<?> customerEndpoint() {
        return ResponseEntity.ok("Customer endpoint çalışıyor!");
    }
    
    @GetMapping("/restaurant")
    public ResponseEntity<?> restaurantEndpoint() {
        return ResponseEntity.ok("Restaurant endpoint çalışıyor!");
    }
    
    @GetMapping("/courier")
    public ResponseEntity<?> courierEndpoint() {
        return ResponseEntity.ok("Courier endpoint çalışıyor!");
    }
} 