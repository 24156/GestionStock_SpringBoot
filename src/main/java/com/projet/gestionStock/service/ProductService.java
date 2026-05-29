package com.projet.gestionStock.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.projet.gestionStock.model.Category;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.model.User;
import com.projet.gestionStock.repository.CategoryRepository;
import com.projet.gestionStock.repository.ProductRepository;
import com.projet.gestionStock.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    final private ProductRepository productRepository;
    final private CategoryRepository categoryRepository;
    final private UserRepository userRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
        .orElseThrow(() -> productNotFoundException(id));
    }

    public Product createProduct(Map<String, Object> request){

        String name = (String) request.get("name");
        Double price = Double.valueOf(request.get("price").toString());
        Integer stock = Integer.valueOf(request.get("stock").toString());
        Integer minStock = Integer.valueOf(request.get("minStock").toString());

        Long categoryId = Long.valueOf(request.get("categoryId").toString());
        Long userId = Long.valueOf(request.get("userId").toString());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setMinStock(minStock);
        product.setCategory(category);
        product.setUser(user);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
        Product pr = productRepository.findById(id)
        .orElseThrow(() -> productNotFoundException(id));

        productRepository.delete(pr);
    }

    private RuntimeException productNotFoundException(Long id){
        return new RuntimeException("Product not found with id: " + id);
    }
    
}
