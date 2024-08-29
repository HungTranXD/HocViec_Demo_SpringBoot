package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends _GenericRepository<Product>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.deletedAt IS NULL")
    List<Product> findAllWithCategory();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id IN ?1")
    List<Product> findAllByIdWithLock(Collection<Long> ids);

    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

}
