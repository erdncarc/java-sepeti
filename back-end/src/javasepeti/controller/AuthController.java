package com.example.javasepeti.controller;

import com.example.javasepeti.dto.auth.LoginRequestDTO;
import com.example.javasepeti.dto.auth.LoginResponseDTO;
import com.example.javasepeti.dto.auth.MessageResponseDTO;
import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.enums.UserRole;
import com.example.javasepeti.security.JwtUtil;
import com.example.javasepeti.service.AdminService;
import com.example.javasepeti.service.CourierService;
import com.example.javasepeti.service.CustomerService;
import com.example.javasepeti.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminService adminService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final CourierService courierService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          AdminService adminService, CustomerService customerService,
                          RestaurantService restaurantService, CourierService courierService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminService = adminService;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.courierService = courierService;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUsername(userDetails.getUsername());
        response.setToken(token);
        response.setRole(UserRole.fromRoleName(roles));


        return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@Valid @RequestBody RegisterDTO registerRequest) {

        switch (registerRequest.getRole()) {
            case CUSTOMER -> customerService.createCustomerAccount(registerRequest);
            case RESTAURANT -> restaurantService.createRestaurantAccount(registerRequest);
            case COURIER -> courierService.createCourierAccount(registerRequest);
            default -> {
                return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid user role"));
            }
        }
        return ResponseEntity.ok(new MessageResponseDTO("User saved successfully."));

    }
}