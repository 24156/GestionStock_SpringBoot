package com.projet.gestionStock.dto.request;

import lombok.Data;

@Data
public class SupplierRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
}