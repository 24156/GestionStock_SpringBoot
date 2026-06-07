package com.projet.gestionStock.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private Integer minStock;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private LocalDateTime createdAt;

    private Long supplierId;
    private String supplierName;
}