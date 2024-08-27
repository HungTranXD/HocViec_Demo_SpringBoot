package com.example.springbootdemo.dto.order;

import com.example.springbootdemo.dto.product.ProductResponse;
import com.example.springbootdemo.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private ProductResponse product;
    private Double buyPrice;
    private Integer buyQuantity;
}
