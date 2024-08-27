package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.entity.Product;
import com.example.springbootdemo.repository.ProductRepository;
import com.example.springbootdemo.repository._GenericRepository;
import com.example.springbootdemo.service.ProductService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends _GenericServiceImpl<Product> implements ProductService {

    public ProductServiceImpl(_GenericRepository<Product> repository) {
        super(repository);
    }

    @Override
    public List<Product> findAll() {
        return ((ProductRepository) repository).findAll();
    }

    @Override
    public Product findByName(String name) {
        return ((ProductRepository) repository).findByName(name);
    }

    @Override
    public List<Product> findAllWithSpecifications(Specification<Product> spec) {
        return ((ProductRepository) repository).findAll(spec);
    }

}
