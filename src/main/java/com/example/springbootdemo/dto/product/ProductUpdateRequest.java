package com.example.springbootdemo.dto.product;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Long categoryId;
}

