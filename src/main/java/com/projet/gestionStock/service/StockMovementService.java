package com.projet.gestionStock.service;

import com.projet.gestionStock.dto.request.StockMovementRequest;
import com.projet.gestionStock.dto.response.StockMovementResponse;
import com.projet.gestionStock.exception.BadRequestException;
import com.projet.gestionStock.exception.ResourceNotFoundException;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.model.StockMovement;
import com.projet.gestionStock.model.User;
import com.projet.gestionStock.model.enums.StockMovementType;
import com.projet.gestionStock.repository.ProductRepository;
import com.projet.gestionStock.repository.StockMovementRepository;
import com.projet.gestionStock.repository.UserRepository;
import com.projet.gestionStock.specification.StockMovementSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public StockMovementResponse createMovement(StockMovementRequest request, String username) {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        int updatedStock = product.getStock();

        if (request.getType() == StockMovementType.IN) {
            updatedStock += request.getQuantity();
        } else if (request.getType() == StockMovementType.OUT) {
            if (product.getStock() < request.getQuantity()) {
                throw new BadRequestException("Incomplete stock! Available quantity: " + product.getStock());
            }
            updatedStock -= request.getQuantity();
        } else if (request.getType() == StockMovementType.ADJUSTMENT) {
            
            updatedStock = request.getQuantity();
            if (updatedStock < 0) {
                throw new BadRequestException("Stock quantity cannot be negative during adjustment");
            }
        }

        
        product.setStock(updatedStock);
        productRepository.save(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setUser(user);
        movement.setQuantity(request.getQuantity());
        movement.setType(request.getType());
        movement.setReason(request.getReason());

        StockMovement savedMovement = stockMovementRepository.save(movement);

        return mapToResponse(savedMovement);
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> getFilteredMovements(String productName, StockMovementType type, String reason) {
        Specification<StockMovement> spec = StockMovementSpecification.filterMovements(productName, type, reason);
        return stockMovementRepository.findAll(spec).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByProductId(Long productId) {
        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    private StockMovementResponse mapToResponse(StockMovement sm) {
        return new StockMovementResponse(
                sm.getId(),
                sm.getProduct().getId(),
                sm.getProduct().getName(),
                sm.getUser().getId(),
                sm.getUser().getUsername(),
                sm.getQuantity(),
                sm.getType(),
                sm.getReason(),
                sm.getCreatedAt()
        );
    }
}