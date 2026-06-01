package com.projet.gestionStock.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Double price;
    private Integer stock;
    private Integer minStock;
    private Long categoryId;
}