package com.projet.gestionStock.service;

import com.projet.gestionStock.dto.response.DashboardStatsResponse;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.model.StockMovement;
import com.projet.gestionStock.model.enums.StockMovementType;
import com.projet.gestionStock.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        
        long totalProducts = productRepository.count();
        long totalCategories = categoryRepository.count();
        long totalSuppliers = supplierRepository.count();

        List<Product> allProducts = productRepository.findAll();

        double totalInventoryValue = allProducts.stream()
        .mapToDouble(p -> p.getStock() * p.getPrice())
        .sum();

        long lowStockCount = allProducts.stream()
        .filter(p -> p.getStock() <= p.getMinStock())
        .count();

        // Track stock movements for the current day
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<StockMovement> todayMovements = stockMovementRepository.findAll().stream()
        .filter(m -> m.getCreatedAt() != null && 
                    !m.getCreatedAt().isBefore(startOfToday) && 
                    !m.getCreatedAt().isAfter(endOfToday))
        .toList();

        long todayIn = todayMovements.stream().filter(m -> m.getType() == StockMovementType.IN).count();
        long todayOut = todayMovements.stream().filter(m -> m.getType() == StockMovementType.OUT).count();


        // Calculate category stock percentages for Flutter Pie Chart
        long totalPhysicalStock = allProducts.stream().mapToLong(Product::getStock).sum();

        Map<String, Double> categoryStockPercentages;

        if (totalPhysicalStock == 0) {
            categoryStockPercentages = Map.of();
        } else {
            // Group total physical stock units by category name
            Map<String, Long> stockByCategory = allProducts.stream()
                .filter(p -> p.getCategory() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getCategory().getName(),
                        Collectors.summingLong(Product::getStock)
            ));

            // Transform grouped volume to exact percentages safely
            categoryStockPercentages = stockByCategory.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            double percentage = ((double) entry.getValue() / totalPhysicalStock) * 100;
                            
                            return (double) Math.round(percentage * 100) / 100;
                        }
            ));
        }

        // [We will add the final return block here in the next step]
        
        return new DashboardStatsResponse(
            totalProducts,
            totalCategories,
            totalSuppliers,
            totalInventoryValue,
            lowStockCount,
            todayIn,
            todayOut,
            categoryStockPercentages
        );
    }
}