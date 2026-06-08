package com.projet.gestionStock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import com.projet.gestionStock.dto.request.ProductRequest;
import com.projet.gestionStock.dto.response.ProductResponse;
import com.projet.gestionStock.service.ProductService;



@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
            
        return ResponseEntity.ok(productService.getFilteredProducts(name, minPrice, maxPrice, categoryId, page, size));
    }

    @GetMapping("/minstock")
    public ResponseEntity<List<ProductResponse>> getAllLowStockProducts() {
        return ResponseEntity.ok(productService.getAllLowStockProducts());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody ProductRequest req,
        @AuthenticationPrincipal(expression = "username") String username) {

        ProductResponse res = productService.createProduct(req, username);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductRequest req) {
        
        ProductResponse res = productService.updateProduct(id, req);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
