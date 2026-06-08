package com.projet.gestionStock.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    
    private long totalProducts;
    private long totalCategories;
    private long totalSuppliers;
    private double totalInventoryValue;
    private long lowStockProductsCount;
    
    
    private long todayInMovements;
    private long todayOutMovements;

    private Map<String, Double> categoryStockPercentages;
}