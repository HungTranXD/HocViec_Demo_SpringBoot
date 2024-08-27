package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.order.OrderCreateRequest;
import com.example.springbootdemo.dto.order.OrderResponse;
import com.example.springbootdemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable String id) {
        return orderService.findById(id);
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderCreateRequest order) {
        return orderService.createOrder(order);
    }
}
