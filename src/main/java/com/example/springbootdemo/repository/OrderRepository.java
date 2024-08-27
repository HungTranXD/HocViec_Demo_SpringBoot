package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails WHERE o.id = ?1")
    Optional<Order> findById(String id);
}
