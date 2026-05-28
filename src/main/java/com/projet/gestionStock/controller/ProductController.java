package com.projet.gestionStock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.repository.ProductRepository;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Product pr = productRepository.findById(id)
        .orElseThrow(() ->  productNotFoundException(id));
        return ResponseEntity.ok(pr);
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product pr){
        Product savePr = productRepository.save(pr);
        return new ResponseEntity<>(savePr,HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        Product pr = productRepository.findById(id)
        .orElseThrow(() -> productNotFoundException(id));
        productRepository.delete(pr);
        return ResponseEntity.noContent().build();
    }

    protected RuntimeException productNotFoundException(Long id){
        return new RuntimeException("Product not found with id: " + id);
    }

}
