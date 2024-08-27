package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.entity.Category;
import com.example.springbootdemo.repository._GenericRepository;
import com.example.springbootdemo.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryServiceImpl extends _GenericServiceImpl<Category> implements CategoryService {
    public CategoryServiceImpl(_GenericRepository<Category> categoryRepository) {
        super(categoryRepository);
    }

    @Override
    public void permanentDelete(Long id) {
        Optional<Category> optional = findById(id);
        optional.ifPresent(repository::delete);
    }

    //Create 2 methods to test Transactional
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void testTransactional() {
        Category category = new Category();
        category.setName("Test Category 1.1");
        repository.save(category);

        testTransactional2();

        Category category2 = new Category();
        category2.setName("Test Category 1.2");
        repository.save(category2);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void testTransactional2() {
        Category category = new Category();
        category.setName("Test Transactional Category 2");
        repository.save(category);
    }

}
