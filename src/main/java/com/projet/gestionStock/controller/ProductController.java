package com.projet.gestionStock.controller;


import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.projet.gestionStock.model.Product;
import com.projet.gestionStock.repository.ProductRepository;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    @GetMapping
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable long id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("produit introuvable"));
    }


    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productRepository.save(product);
    }


    @DeleteMapping("/{id}")
    public Product deleteProduct(@PathVariable long id){
        Product product=productRepository.findById(id).orElseThrow(() -> new RuntimeException("le produit n'est trouve pas"));
        productRepository.delete(product);
        return product;
    }

}
