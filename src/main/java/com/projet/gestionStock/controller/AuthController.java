package com.projet.gestionStock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet.gestionStock.model.User;
import com.projet.gestionStock.service.UserService;

import com.projet.gestionStock.dto.LoginRequest;
import com.projet.gestionStock.dto.AuthResponse;




import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){

        User newUser = userService.register(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        AuthResponse  authresponse = userService.login(request);

        return ResponseEntity.ok(authresponse);
}

}