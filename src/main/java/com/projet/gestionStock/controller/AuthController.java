package com.projet.gestionStock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet.gestionStock.dto.response.UserResponse;
import com.projet.gestionStock.service.UserService;
import com.projet.gestionStock.dto.request.LoginRequest;
import com.projet.gestionStock.dto.request.RegisterRequest;
import com.projet.gestionStock.dto.response.AuthResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest req){

        UserResponse res = userService.register(req);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        AuthResponse res = userService.login(req);
        return ResponseEntity.ok(res);
    }

}