package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.category.CategoryResponse;
import com.example.springbootdemo.entity.Category;
import com.example.springbootdemo.entity.Product;
import com.example.springbootdemo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getCategories() {
        return categoryService.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategory(@PathVariable Long id) {
        return modelMapper.map(categoryService.findById(id).orElse(null), CategoryResponse.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public Category test() {
        Category category = new Category();
        category.setName("Test Category");

        List<Product> products = new ArrayList<>(
            List.of(
                Product.builder().name("Test Product 1").price(100.00).build(),
                Product.builder().name("Test Product 2").price(150.00).build()
            )
        );

        category.setProducts(products);

        return categoryService.save(category);
    }

    // Test CascadeType.MERGE
    @GetMapping("/test2")
    @ResponseStatus(HttpStatus.OK)
    public Category test2() {
        Category searchCategory = categoryService.findById(1L).orElse(null);
        searchCategory.setName("Updated Category");

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(200.00);

        searchCategory.getProducts().add(product);

        return categoryService.save(searchCategory);
    }

    // Test CascadeType.REMOVE
    @GetMapping("/test3")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void test3() {
        categoryService.permanentDelete(1L);
    }

    // Test orphanRemoval
    @GetMapping("/test4")
    @ResponseStatus(HttpStatus.OK)
    public Category test4() {
        // Fetch the existing category from the database
        Category category = categoryService.findById(1L).orElse(null);

        // Remove a product from the category's products list
        if (category != null && !category.getProducts().isEmpty()) {
            category.getProducts().removeFirst(); // Remove the first product
        }

        // Save the category (this will also remove the orphaned product due to orphanRemoval = true)
        return categoryService.save(category);
    }


    //Test transactional:
    @GetMapping("/test-transactional")
    @ResponseStatus(HttpStatus.OK)
    public void testTransactional() {
        categoryService.testTransactional();
    }


}
