package com.projet.gestionStock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projet.gestionStock.dto.request.ProductRequest;
import com.projet.gestionStock.dto.response.ProductResponse;
import com.projet.gestionStock.exception.ResourceNotFoundException;
import com.projet.gestionStock.model.Category;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.model.User;
import com.projet.gestionStock.model.Supplier;
import com.projet.gestionStock.repository.CategoryRepository;
import com.projet.gestionStock.repository.ProductRepository;
import com.projet.gestionStock.repository.UserRepository;
import com.projet.gestionStock.repository.SupplierRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; 
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository; 

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(pr -> new ProductResponse(
                        pr.getId(),
                        pr.getName(),
                        pr.getPrice(),
                        pr.getStock(),
                        pr.getMinStock(),
                        pr.getCategory().getId(),
                        pr.getCategory().getName(),
                        pr.getUser().getId(),
                        pr.getCreatedAt(),
                        pr.getSupplier() != null ? pr.getSupplier().getId() : null,  
                        pr.getSupplier() != null ? pr.getSupplier().getName() : null  
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product pr = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return new ProductResponse(
                pr.getId(), 
                pr.getName(),
                pr.getPrice(),
                pr.getStock(), 
                pr.getMinStock(), 
                pr.getCategory().getId(),
                pr.getCategory().getName(),
                pr.getUser().getId(),
                pr.getCreatedAt(),
                pr.getSupplier() != null ? pr.getSupplier().getId() : null,  
                pr.getSupplier() != null ? pr.getSupplier().getName() : null 
        );
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        
        Supplier supplier = null;
        if (request.getSupplierId() != null) {
            supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + request.getSupplierId()));
        }
        
        Product pr = new Product();
        pr.setName(request.getName());
        pr.setPrice(request.getPrice());
        pr.setStock(request.getStock());
        pr.setMinStock(request.getMinStock());
        pr.setCategory(category); 
        pr.setUser(user);
        pr.setSupplier(supplier); 

        Product savePr = productRepository.save(pr);

        return new ProductResponse(
                savePr.getId(),
                savePr.getName(),
                savePr.getPrice(),
                savePr.getStock(),
                savePr.getMinStock(),
                savePr.getCategory().getId(),
                savePr.getCategory().getName(),
                savePr.getUser().getId(),
                savePr.getCreatedAt(),
                savePr.getSupplier() != null ? savePr.getSupplier().getId() : null,   
                savePr.getSupplier() != null ? savePr.getSupplier().getName() : null  
        );
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product pr = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(pr);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getFilteredProducts(String name, Double minPrice, Double maxPrice, Long categoryId) {
        org.springframework.data.jpa.domain.Specification<Product> spec = 
                com.projet.gestionStock.specification.ProductSpecification.filterProducts(name, minPrice, maxPrice, categoryId);
        
        return productRepository.findAll(spec).stream()
        .map(pr -> new ProductResponse(
                pr.getId(),
                pr.getName(),
                pr.getPrice(),
                pr.getStock(),
                pr.getMinStock(),
                pr.getCategory().getId(),
                pr.getCategory().getName(),
                pr.getUser().getId(),
                pr.getCreatedAt(),
                pr.getSupplier() != null ? pr.getSupplier().getId() : null,
                pr.getSupplier() != null ? pr.getSupplier().getName() : null            
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllLowStockProducts(){
        return productRepository.findLowStockProducts().stream()
        .map(pr -> new ProductResponse(
                pr.getId(),
                pr.getName(),
                pr.getPrice(),
                pr.getStock(),
                pr.getMinStock(),
                pr.getCategory().getId(),
                pr.getCategory().getName(),
                pr.getUser().getId(),
                pr.getCreatedAt(),
                pr.getSupplier() != null ? pr.getSupplier().getId() : null,   
                pr.getSupplier() != null ? pr.getSupplier().getName() : null  
        )).collect(Collectors.toList());
    }
}