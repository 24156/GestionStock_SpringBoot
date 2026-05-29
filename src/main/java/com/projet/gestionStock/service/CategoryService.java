package com.projet.gestionStock.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.projet.gestionStock.model.Category;
import com.projet.gestionStock.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() ->  categoryNotFoundException(id));
    }

    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id){
        Category ctg = categoryRepository.findById(id)
                .orElseThrow(() -> categoryNotFoundException(id));

        categoryRepository.delete(ctg);
    }

    private RuntimeException categoryNotFoundException(Long id){
        return new RuntimeException("Category not found with id: " + id);
    }
}