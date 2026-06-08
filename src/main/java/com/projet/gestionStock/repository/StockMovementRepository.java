package com.projet.gestionStock.repository;

import com.projet.gestionStock.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> , JpaSpecificationExecutor<StockMovement>{
    
    List<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId);
}