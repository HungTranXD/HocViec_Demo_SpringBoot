package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dto.order.OrderCreateRequest;
import com.example.springbootdemo.dto.order.OrderDetailCreateRequest;
import com.example.springbootdemo.dto.order.OrderResponse;
import com.example.springbootdemo.entity.Order;
import com.example.springbootdemo.entity.OrderDetail;
import com.example.springbootdemo.entity.OrderDetailId;
import com.example.springbootdemo.entity.Product;
import com.example.springbootdemo.repository.OrderRepository;
import com.example.springbootdemo.repository.ProductRepository;
import com.example.springbootdemo.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        // STEP 1: Check each product in orderDetails
        HashMap<Long, OrderDetailCreateRequest> orderDetailMap = request.getOrderDetails().stream()
            .collect(
                HashMap::new,
                (map, orderDetail) -> map.put(orderDetail.getProductId(), orderDetail),
                HashMap::putAll
            );
        List<Product> products = productRepository.findAllById(orderDetailMap.keySet());

        // -- 1.1: Check if product exists
        if (products.size() != orderDetailMap.size()) {
            throw new RuntimeException("Product not found");
        }

        // -- 1.2: Check if product price and quantity are correct
        for (Product product : products) {
            OrderDetailCreateRequest orderDetail = orderDetailMap.get(product.getId());
            if (product.getPrice() != orderDetail.getBuyPrice()) {
                throw new RuntimeException("Product price is not correct");
            }
            if (product.getQuantity() < orderDetail.getBuyQuantity()) {
                throw new RuntimeException("Product quantity is not enough");
            }
        }

        // STEP 2: Create Order:
        // -- 2.1: Create new Order instance
        Order order = modelMapper.map(request, Order.class);

        // -- 2.2: Create OrderDetail instances
        List<OrderDetail> orderDetails = products.stream()
            .map(product -> OrderDetail.builder()
                .id(new OrderDetailId(order, product))
                .buyPrice(orderDetailMap.get(product.getId()).getBuyPrice())
                .buyQuantity(orderDetailMap.get(product.getId()).getBuyQuantity())
                .build()
            )
            .toList();
        order.setOrderDetails(orderDetails);

        // -- 2.3: Update total
        order.setTotal(
            request.getOrderDetails().stream()
                .mapToDouble(OrderDetailCreateRequest::getBuyPrice)
                .sum()
        );

        // -- 2.4: Save order
        order.setStatus("PENDING");
        orderRepository.save(order);

        // STEP 3: Update product quantity:
        for (Product product : products) {
            OrderDetailCreateRequest orderDetail = orderDetailMap.get(product.getId());
            product.setQuantity(product.getQuantity() - orderDetail.getBuyQuantity());
        }
        productRepository.saveAll(products);

        // STEP 4: Return OrderResponse:
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> findAll() {
        return List.of();
    }

    @Override
    public OrderResponse findById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderResponse.class);
    }

}
