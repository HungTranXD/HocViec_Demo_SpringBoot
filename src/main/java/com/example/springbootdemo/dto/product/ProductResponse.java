package com.example.springbootdemo.dto.product;

import com.example.springbootdemo.dto.category.CategoryResponse;
import com.example.springbootdemo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private LocalDateTime createdAt;
    private CategoryResponse category;
}
