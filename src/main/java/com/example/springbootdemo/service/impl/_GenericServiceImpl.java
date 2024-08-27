package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.entity._BaseEntity;
import com.example.springbootdemo.repository._GenericRepository;
import com.example.springbootdemo.service._GenericService;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class _GenericServiceImpl<T extends _BaseEntity> implements _GenericService<T, Long> {

    protected final _GenericRepository<T> repository;

    @Override
    public List<T> findAll() {
        return repository.findAllByDeletedAtIsNull();
    }

    @Override
    public List<T> findDeleted() {
        return repository.findAllByDeletedAtIsNotNull();
    }

    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    @Transactional
    public T update(T entity) {
        Optional<T> optional = repository.findById(entity.getId());
        if (optional.isEmpty()) {
            return null;
        }
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<T> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return;
        }
        T entity = optional.get();
        entity.softDelete();
        repository.save(entity);
    }

    @Override
    @Transactional
    public void restore(T entity) {
        entity.setDeletedAt(null);
        repository.save(entity);
    }
}
