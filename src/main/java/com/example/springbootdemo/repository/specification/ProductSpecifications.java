package com.example.springbootdemo.repository.specification;

import com.example.springbootdemo.entity.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> hasDeleted(Boolean deleted) {
        return (root, query, builder) ->
            deleted == null ? null : deleted ? builder.isNotNull(root.get("deletedAt")) : builder.isNull(root.get("deletedAt"));
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, builder) ->
            name == null ? null : builder.equal(root.get("name"), name);
    }

    public static Specification<Product> hasCategoryIds(List<Integer> categoryIds) {
        return (root, query, builder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return null;
            }
            return root.get("category").get("id").in(categoryIds);
        };
    }

    public static Specification<Product> hasPriceGreaterThan(Double price) {
        return (root, query, builder) ->
            price == null ? null : builder.greaterThan(root.get("price"), price);
    }

    public static Specification<Product> hasPriceLessThan(Double price) {
        return (root, query, builder) ->
            price == null ? null : builder.lessThan(root.get("price"), price);
    }

    public static Specification<Product> inStock(Boolean inStock) {
        return (root, query, builder) ->
            inStock == null ? null : builder.greaterThan(root.get("quantity"), 0);
    }
}
