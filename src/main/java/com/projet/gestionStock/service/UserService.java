package com.projet.gestionStock.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projet.gestionStock.model.User;
import com.projet.gestionStock.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user){

        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists.");
        }

        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}