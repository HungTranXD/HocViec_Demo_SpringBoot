package com.example.springbootdemo.dto.order;

import com.example.springbootdemo.entity.OrderDetail;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    private String shippingName;
    private String shippingAddress;
    private String shippingPhone;
    private String paymentMethod;
    private List<OrderDetailCreateRequest> orderDetails;
}

