package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dto.product.ProductCreateRequest;
import com.example.springbootdemo.dto.product.ProductResponse;
import com.example.springbootdemo.dto.product.ProductUpdateRequest;
import com.example.springbootdemo.entity.Category;
import com.example.springbootdemo.entity.Product;
import com.example.springbootdemo.repository.CategoryRepository;
import com.example.springbootdemo.repository.ProductRepository;
import com.example.springbootdemo.repository._GenericRepository;
import com.example.springbootdemo.repository.specification.ProductSpecifications;
import com.example.springbootdemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
// @CacheConfig(cacheNames = "products") //You can do this instead of specifying cache names in every method
public class ProductServiceImpl extends _GenericServiceImpl<Product> implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(
            _GenericRepository<Product> repository,
            CategoryRepository categoryRepository,
            ModelMapper modelMapper
    ) {
        super(repository);
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Cacheable(value = "products", cacheManager = "redisCacheManager")
    public List<ProductResponse> findAllWithSpecifications(String name, List<Integer> categoryIds, Double minPrice, Double maxPrice, Boolean inStock) {
        Specification<Product> spec = Specification.where(ProductSpecifications.hasDeleted(false))
                .and(ProductSpecifications.hasName(name))
                .and(ProductSpecifications.hasCategoryIds(categoryIds))
                .and(ProductSpecifications.hasPriceGreaterThan(minPrice))
                .and(ProductSpecifications.hasPriceLessThan(maxPrice))
                .and(ProductSpecifications.inStock(inStock));
        List<Product> products = ((ProductRepository) repository).findAll(spec);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
    }

    @Override
    public List<ProductResponse> findAllByIdWithLock(List<Long> ids) {
        return ((ProductRepository) repository).findAllByIdWithLock(ids).stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        log.info("Creating product: {}", request.toString());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        Product product = modelMapper.map(request, Product.class);
        product.setCategory(category);
         repository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @CachePut(value = "products", key = "'product_' + #request.id", cacheManager = "redisCacheManager")
    public ProductResponse updateProduct(ProductUpdateRequest request) {
        Product product = repository.findById(request.getId()).orElseThrow();
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();

        modelMapper.map(request, product);
        product.setCategory(category);
        repository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @Cacheable(value = "products", key = "'product_' + #id", cacheManager = "redisCacheManager", sync = true)
    public ProductResponse findProductById(Long id) {
        try {
            log.info("Simulating long process...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.debug(e.getMessage());
        }
        return ((ProductRepository) repository).findByIdAndDeletedAtIsNull(id)
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .orElse(null);
    }

    @Override
    @CacheEvict(value = "products", key = "'product_' + #id", cacheManager = "redisCacheManager")
    public void deleteById(Long id) {
        Product product = repository.findById(id).orElseThrow();
        product.softDelete();
        repository.save(product);
    }


    /*
        - We need a method just for evicting the cache.
        - This method will be called by other methods that need to evict the cache.
     */
    @Override
    @CacheEvict(value = "products", key = "'product_' + #id", cacheManager = "redisCacheManager")
    public void evictCache(Long id) {
        log.info("Evicting cache for product with id: {}", id);
    }
}
