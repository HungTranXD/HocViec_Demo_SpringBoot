package com.example.springbootdemo.config;

import com.example.springbootdemo.dto.order.OrderDetailResponse;
import com.example.springbootdemo.entity.OrderDetail;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
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

        return modelMapper;
    }
}
