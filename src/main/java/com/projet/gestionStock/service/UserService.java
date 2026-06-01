package com.projet.gestionStock.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projet.gestionStock.dto.request.LoginRequest;
import com.projet.gestionStock.dto.request.RegisterRequest;
import com.projet.gestionStock.dto.response.AuthResponse;
import com.projet.gestionStock.model.User;
import com.projet.gestionStock.repository.UserRepository;
import com.projet.gestionStock.dto.response.UserResponse;
import com.projet.gestionStock.exception.BadRequestException;
import com.projet.gestionStock.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request){

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already exists.");
        }

        if(userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("Username already exists.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }


    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
         User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }

}