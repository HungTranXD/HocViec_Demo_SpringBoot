package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.order.OrderCreateRequest;
import com.example.springbootdemo.dto.order.OrderResponse;
import com.example.springbootdemo.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest order);
    List<OrderResponse> findAll();
    OrderResponse findById(String id);
}
