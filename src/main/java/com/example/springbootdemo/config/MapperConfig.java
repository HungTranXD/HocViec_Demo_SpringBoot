package com.example.springbootdemo.config;

import com.example.springbootdemo.dto.order.OrderDetailResponse;
import com.example.springbootdemo.dto.product.ProductCreateRequest;
import com.example.springbootdemo.dto.product.ProductUpdateRequest;
import com.example.springbootdemo.entity.OrderDetail;
import com.example.springbootdemo.entity.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping for OrderDetail to OrderDetailResponse
        modelMapper.addMappings(new PropertyMap<OrderDetail, OrderDetailResponse>() {
            @Override
            protected void configure() {
                // Map the embedded Product in OrderDetailId to ProductResponse
                map(source.getId().getProduct(), destination.getProduct());
            }
        });

        // Custom mapping for ProductCreateRequest to Product
        modelMapper.addMappings(new PropertyMap<ProductCreateRequest, Product>() {
            @Override
            protected void configure() {
                // Skip the id field in ProductCreateRequest
                skip().setId(null);
            }
        });

        // Custom mapping for ProductUpdateRequest to Product
        modelMapper.addMappings(new PropertyMap<ProductUpdateRequest, Product>() {
            @Override
            protected void configure() {
                // Skip the category field in ProductUpdateRequest
                skip().setCategory(null);
            }
        });

        return modelMapper;
    }

}
