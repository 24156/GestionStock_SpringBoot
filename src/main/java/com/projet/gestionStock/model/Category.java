package com.projet.gestionStock.model;

import jakarta.persistence.*;

@Entity
@table(name="category")
public class Category {
    @Id
    private long id;
    private String name;
    private String description;


    
    public Category() {
    }


    private static long idCount=1;

    public Category(String name, String description) {
        this.id=idCount++;
        this.name = name;
        this.description = description;
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


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }




    
}
