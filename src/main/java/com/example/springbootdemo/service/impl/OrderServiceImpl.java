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
import com.example.springbootdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
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
        List<Product> products = productRepository.findAllByIdWithLock(orderDetailMap.keySet());

        // -- 1.1: Check if product exists
        if (products.size() != orderDetailMap.size()) {
            throw new RuntimeException("Product(s) not found");
        }

        // -- 1.2: Check if product quantity are correct
        if (!products.stream().allMatch(product -> product.getQuantity() >= orderDetailMap.get(product.getId()).getBuyQuantity())) {
            throw new RuntimeException("Product quantity is not enough");
        }


        // Simulate long process
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        // STEP 2: Create Order:
        // -- 2.1: Create new Order instance
        Order order = modelMapper.map(request, Order.class);

        // -- 2.2: Create OrderDetail instances
        List<OrderDetail> orderDetails = products.stream()
            .map(product -> OrderDetail.builder()
                .id(new OrderDetailId(order, product))
                .buyPrice(product.getPrice())
                .buyQuantity(orderDetailMap.get(product.getId()).getBuyQuantity())
                .build()
            )
            .toList();
        order.setOrderDetails(orderDetails);

        // -- 2.3: Update total
        order.setTotal(
            orderDetails.stream()
                .mapToDouble(orderDetail -> orderDetail.getBuyPrice() * orderDetail.getBuyQuantity())
                .sum()
        );

        // -- 2.4: Save order
        orderRepository.save(order);

        // STEP 3: Update product quantity:
        for (Product product : products) {
            OrderDetailCreateRequest orderDetail = orderDetailMap.get(product.getId());
            product.setQuantity(product.getQuantity() - orderDetail.getBuyQuantity());
        }
        productRepository.saveAll(products);
        products.forEach(product -> productService.evictCache(product.getId()));

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
