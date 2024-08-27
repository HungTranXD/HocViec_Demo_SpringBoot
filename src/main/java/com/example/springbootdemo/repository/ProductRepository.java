package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends _GenericRepository<Product>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Product findByName(String name);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.deletedAt IS NULL")
    List<Product> findAllWithCategory();
}
