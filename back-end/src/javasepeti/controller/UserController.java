package com.example.javasepeti.controller;


import com.example.javasepeti.dto.auth.MessageResponseDTO;
import com.example.javasepeti.enums.AccountStatus;
import com.example.javasepeti.model.Restaurant;
import com.example.javasepeti.model.User;
import com.example.javasepeti.security.CustomUserDetails;
import com.example.javasepeti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService<User> userService;

    @Autowired
    public UserController(UserService<User> userService) {
        this.userService = userService;
    }

    @DeleteMapping("/me")
    public ResponseEntity<MessageResponseDTO> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteAccount(userDetails.getId());
        return ResponseEntity.ok(new MessageResponseDTO("Account successfully deleted."));
    }


}
