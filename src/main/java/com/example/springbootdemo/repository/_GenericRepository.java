package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity._BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface _GenericRepository<T extends _BaseEntity> extends JpaRepository<T, Long> {
    List<T> findAllByDeletedAtIsNull();
    List<T> findAllByDeletedAtIsNotNull();
}
