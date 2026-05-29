package com.projet.gestionStock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false,length = 50)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "min_stock")
    private Integer minStock;

    @ManyToOne(fetch = FetchType.LAZY)
    
    @JoinColumn(nullable = false, name = "category_id")
    @JsonIgnoreProperties({"products", "hibernateLazyInitializer", "handler"})
    private Category category;

    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore 
    private User user;

    @com.fasterxml.jackson.annotation.JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    

}
