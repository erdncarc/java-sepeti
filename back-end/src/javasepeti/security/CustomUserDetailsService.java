package com.example.javasepeti.security;

import com.example.javasepeti.enums.UserRole;
import com.example.javasepeti.model.User;
import com.example.javasepeti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository<User> userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        UserRole role;
        switch (user.getClass().getSimpleName()) {
            case "Admin" -> role = UserRole.ADMIN;
            case "Customer" -> role = UserRole.CUSTOMER;
            case "Restaurant" -> role = UserRole.RESTAURANT;
            case "Courier" -> role = UserRole.COURIER;
            default -> throw new UsernameNotFoundException("Geçersiz kullanıcı tipi");
        }

        return new CustomUserDetails(user, role);
    }
}
