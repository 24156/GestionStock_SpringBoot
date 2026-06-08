package com.projet.gestionStock.specification;

import com.projet.gestionStock.model.StockMovement;
import com.projet.gestionStock.model.enums.StockMovementType;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class StockMovementSpecification {

    public static Specification<StockMovement> filterMovements(String productName, StockMovementType type, String reason) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("product").get("name")), 
                    "%" + productName.toLowerCase() + "%"
                ));
            }

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (reason != null && !reason.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("reason")), 
                    "%" + reason.toLowerCase() + "%"
                ));
            }

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}