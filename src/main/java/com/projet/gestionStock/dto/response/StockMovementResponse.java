package com.projet.gestionStock.dto.response;

import com.projet.gestionStock.model.enums.StockMovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementResponse {
    private Long id;                    
    
    private Long productId;
    private String productName;
    
    private Long userId;
    private String username;
    
    private Integer quantity;
    private StockMovementType type;
    private String reason;
    private LocalDateTime createdAt;
}