package com.projet.gestionStock.dto.request;

import com.projet.gestionStock.model.enums.StockMovementType;

import lombok.Data;

@Data
public class StockMovementRequest {
    private Long productId;             
    private Integer quantity;           
    private StockMovementType type;     
    private String reason;              
}