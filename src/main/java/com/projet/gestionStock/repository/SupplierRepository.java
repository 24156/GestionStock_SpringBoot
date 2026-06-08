package com.projet.gestionStock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projet.gestionStock.model.Supplier;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByUserUsernameAndActiveTrue(String username);

    Optional<Supplier> findByIdAndUserUsernameAndActiveTrue(Long id, String username);

}