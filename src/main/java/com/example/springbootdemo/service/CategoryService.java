package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.Category;

public interface CategoryService extends _GenericService<Category, Long> {
    //Create a permanent delete method
    void permanentDelete(Long id);

    void testTransactional();
}
