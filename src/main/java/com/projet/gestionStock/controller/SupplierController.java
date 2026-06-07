package com.projet.gestionStock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

import com.projet.gestionStock.dto.request.SupplierRequest;
import com.projet.gestionStock.dto.response.SupplierResponse;
import com.projet.gestionStock.service.SupplierService;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers(
            @AuthenticationPrincipal(expression = "username") String username) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "username") String username) {
        return ResponseEntity.ok(supplierService.getSupplierById(id, username));
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @RequestBody SupplierRequest req,
            @AuthenticationPrincipal(expression = "username") String username) {
        SupplierResponse res = supplierService.createSupplier(req, username);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierRequest req,
            @AuthenticationPrincipal(expression = "username") String username) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, req, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "username") String username) {
        supplierService.deleteSupplier(id, username);
        return ResponseEntity.noContent().build();
    }
}