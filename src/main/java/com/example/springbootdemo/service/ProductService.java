package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.product.ProductCreateRequest;
import com.example.springbootdemo.dto.product.ProductResponse;
import com.example.springbootdemo.dto.product.ProductUpdateRequest;
import com.example.springbootdemo.entity.Product;

import java.util.List;

public interface ProductService extends _GenericService<Product, Long> {
    List<ProductResponse> findAllWithSpecifications(String name, List<Integer> categoryIds, Double minPrice, Double maxPrice, Boolean inStock);
    List<ProductResponse> findAllByIdWithLock(List<Long> ids);

    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(ProductUpdateRequest request);
    ProductResponse findProductById(Long id);
    void evictCache(Long id);
}
