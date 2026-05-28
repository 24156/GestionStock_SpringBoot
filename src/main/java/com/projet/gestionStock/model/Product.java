package com.projet.gestionStock.model;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name="products")
public class Product {

    @Id
    private long id;
    
    private String name;
    private double price;
    private int stock;

    private int minStock;
    private String categoryId;


    private Date createdAt;


    
    public Product() {
    }
    static int idCount=1;
    public Product(String name, double price, int stock, int minStock, String categoryId) {
        this.id=idCount++;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.minStock = minStock;
        this.categoryId = categoryId;
        this.createdAt=new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    

}
