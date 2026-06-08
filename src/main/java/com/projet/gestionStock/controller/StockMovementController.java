package com.projet.gestionStock.controller;

import com.projet.gestionStock.dto.request.StockMovementRequest;
import com.projet.gestionStock.dto.response.StockMovementResponse;
import com.projet.gestionStock.model.enums.StockMovementType;
import com.projet.gestionStock.service.StockMovementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movements")
@AllArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping
    public ResponseEntity<StockMovementResponse> createMovement(
            @RequestBody StockMovementRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(stockMovementService.createMovement(request, userDetails.getUsername()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StockMovementResponse>> getFilteredMovements(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) StockMovementType type,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(stockMovementService.getFilteredMovements(productName, type, reason));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovementResponse>> getMovementsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(stockMovementService.getMovementsByProductId(productId));
    }
}