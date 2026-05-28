package com.projet.gestionStock.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projet.gestionStock.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{}