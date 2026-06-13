package com.example.javasepeti.service;

import com.example.javasepeti.dto.auth.RegisterDTO;
import com.example.javasepeti.model.User;
import com.example.javasepeti.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService<T extends User> {

    protected final UserRepository<T> userRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository<T> userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    public Optional<T> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    public Optional<T> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByName(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public T findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(String.format("User with id: %d not found",id)
        ));
    }


    protected T add(T user , RegisterDTO registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        return this.save(user);
    }

    public T updateProfilePhoto(Long id, String image) {
        T user = findById(id);
        user.setImage(image);
        return save(user);
    }

    public T save(T user) {
        return userRepository.save(user);
    }


    public void deleteAccount(Long id){
        userRepository.deleteById(id);
    }
} 