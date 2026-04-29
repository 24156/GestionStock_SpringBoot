package com.projet.gestionStock.controller;

import org.springframework.web.bind.annotation.*;
import com.projet.gestionStock.model.Category;
import com.projet.gestionStock.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @GetMapping
    public List<Category> getAllCategorie() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable long id){
        return categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

    }


    @PostMapping
    public Category  createCategory(@RequestBody Category category){
        
        return categoryRepository.save(category);
        }



    @DeleteMapping("/{id}")
    public Category  deleteCategory(@PathVariable long id){
        Category categoryDel =categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Catégorie introuvable"));
        categoryRepository.delete(categoryDel);
        return categoryDel;

    }


}
