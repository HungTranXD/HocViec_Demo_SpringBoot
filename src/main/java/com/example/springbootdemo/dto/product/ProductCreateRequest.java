package com.example.springbootdemo.dto.product;

import com.example.springbootdemo.entity.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Long categoryId;
}

