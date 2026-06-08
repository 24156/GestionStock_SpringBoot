package com.projet.gestionStock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projet.gestionStock.dto.request.CategoryRequest;
import com.projet.gestionStock.dto.response.CategoryResponse;
import com.projet.gestionStock.exception.ResourceNotFoundException;
import com.projet.gestionStock.model.Category;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.model.User;
import com.projet.gestionStock.repository.CategoryRepository;
import com.projet.gestionStock.repository.UserRepository;
import com.projet.gestionStock.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository; 
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id){
        Category ctg = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponse(ctg);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id){
        Category ctg = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        Category defaultCategory = categoryRepository.findByName("Uncategorized")
                .orElseGet(() -> {
                    Category newDefault = new Category();
                    newDefault.setName("Uncategorized");
                    newDefault.setDescription("Default category for orphaned products");
                    newDefault.setUser(ctg.getUser());
                    return categoryRepository.save(newDefault);
                });

        if (ctg.getId().equals(defaultCategory.getId())) {
            throw new IllegalArgumentException("Cannot delete the default 'Uncategorized' category");
        }

        List<Product> products = productRepository.findByCategoryId(ctg.getId());
        if (!products.isEmpty()) {
            for (Product product : products) {
                product.setCategory(defaultCategory);
            }
            productRepository.saveAll(products); 
        }

        categoryRepository.delete(ctg);
    }

    private CategoryResponse mapToResponse(Category ctg) {
        return new CategoryResponse(
                ctg.getId(),
                ctg.getName(),
                ctg.getDescription(),
                ctg.getUser() != null ? ctg.getUser().getId() : null,
                ctg.getCreatedAt()
        );
    }
}