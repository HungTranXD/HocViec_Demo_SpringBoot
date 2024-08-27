package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService extends _GenericService<Product, Long> {
    Product findByName(String name);
    List<Product> findAllWithSpecifications(Specification<Product> spec);
}
