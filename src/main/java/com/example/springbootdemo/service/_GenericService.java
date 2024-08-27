package com.example.springbootdemo.service;

import com.example.springbootdemo.entity._BaseEntity;

import java.util.List;
import java.util.Optional;

public interface _GenericService<T, ID> {
    List<T> findAll();
    List<T> findDeleted();
    Optional<T> findById(ID id);
    T save(T t);
    List<T> saveAll(List<T> t);
    T update(T t);
    void deleteById(ID id);
    void restore(T t);
}
