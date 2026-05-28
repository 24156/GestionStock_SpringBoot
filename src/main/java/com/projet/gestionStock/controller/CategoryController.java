package com.projet.gestionStock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projet.gestionStock.model.Category;
import com.projet.gestionStock.repository.CategoryRepository;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Category ctg = categoryRepository.findById(id)
        .orElseThrow(() -> categoryNotFoundException(id));
        return ResponseEntity.ok(ctg);
    }


    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category ctg = categoryRepository.save(category);
        return new ResponseEntity<>(ctg,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        Category ctg = categoryRepository.findById(id)
        .orElseThrow(() -> categoryNotFoundException(id));
        categoryRepository.delete(ctg);
        return ResponseEntity.noContent().build();
    }

    protected RuntimeException categoryNotFoundException(Long id){
        return new RuntimeException("Category not found with id: " + id);
    }

}
