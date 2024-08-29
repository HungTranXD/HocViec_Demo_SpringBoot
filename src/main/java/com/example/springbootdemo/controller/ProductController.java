package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.product.ProductCreateRequest;
import com.example.springbootdemo.dto.product.ProductResponse;
import com.example.springbootdemo.dto.product.ProductUpdateRequest;
import com.example.springbootdemo.entity.Category;
import com.example.springbootdemo.entity.Product;
import com.example.springbootdemo.repository.specification.ProductSpecifications;
import com.example.springbootdemo.service.CategoryService;
import com.example.springbootdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean inStock)
    {
        return productService.findAllWithSpecifications(name, categoryIds, minPrice, maxPrice, inStock);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }










































    //I need to test CascadeType.PERSIST that I have placed in Product class
    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public Product test() {
        Category category = new Category();
        category.setName("Test Category");

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setCategory(category);

        return productService.save(product);
    }

    @GetMapping("/test2")
    @ResponseStatus(HttpStatus.OK)
    public Product test2() {
        Category category = categoryService.findById(1L).orElse(null);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setCategory(category);

        category.setName("Test Category");

        return productService.save(product);
    }

    //Test CascadeType.MERGE
    @GetMapping("/test3")
    @ResponseStatus(HttpStatus.OK)
    public Product test3() {
        Product product = productService.findById(1L).orElse(null);
        product.setName("Updated Product");

        Category category = new Category();
        category.setId(2L);
        category.setName("Updated Category");

        product.setCategory(category);

        return productService.save(product);
    }

}
