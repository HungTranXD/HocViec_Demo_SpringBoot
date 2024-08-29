package com.example.springbootdemo.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCreateRequest {
    private Long productId;
    private Integer buyQuantity;
}
