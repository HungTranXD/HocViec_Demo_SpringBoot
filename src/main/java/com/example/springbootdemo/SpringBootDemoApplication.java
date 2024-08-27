package com.example.springbootdemo;

import com.example.springbootdemo.entity.*;
import com.example.springbootdemo.repository.OrderRepository;
import com.example.springbootdemo.service.CategoryService;
import com.example.springbootdemo.service.ProductService;
import com.example.springbootdemo.service.UserService;
import com.github.javafaker.Faker;
import org.hibernate.PersistentObjectException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner seedData(
//            UserService userService,
//            ProductService productService,
//            CategoryService categoryService,
//            OrderRepository orderRepository
//    ) {
//        return args -> {
//            Faker faker = new Faker();
//            List<User> users = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                User user = new User();
//                user.setEmail(faker.internet().emailAddress());
//                user.setName(faker.name().fullName());
//                user.setPassword(faker.internet().password());
//                users.add(user);
//            }
//            userService.saveAll(users);
//
//            // Seed categories
//            List<Category> categories = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                Category category = new Category();
//                category.setName(faker.commerce().department());
//                categories.add(category);
//            }
//            categoryService.saveAll(categories);
//
//            // Seed products
//            List<Product> products = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                Product product = new Product();
//                product.setName(faker.commerce().productName());
//                product.setPrice(faker.number().randomDouble(2, 1, 1000));
//                product.setQuantity(faker.number().numberBetween(0, 100));
//                product.setCategory(categories.get(faker.number().numberBetween(0, categories.size() - 1)));
//                products.add(product);
//            }
//            productService.saveAll(products);
//
//            // Seed order:
//            seedOrder(orderRepository, products);
//        };
//    }

    private void seedOrder(
            OrderRepository orderRepository,
            List<Product> products
    ) {
        // Step 1: Create a new Order instance
        Order order = Order.builder()
                .shippingName("John Doe")
                .shippingAddress("123 Main St, Anytown, USA")
                .shippingPhone("123-456-7890")
                .status("PENDING")
                .total(0.0)  // Initial total, will update later
                .paymentMethod("CREDIT_CARD")
                .build();

        // Step 2: Create OrderDetail instances
        List<OrderDetail> orderDetails = IntStream.rangeClosed(1, 5) // For example, associate 5 products
                .mapToObj(i -> {
                    Product product = products.get(i - 1);
                    return OrderDetail.builder()
                            .id(new OrderDetailId(order, product))
                            .buyPrice(product.getPrice())
                            .buyQuantity(1) // or any desired quantity
                            .build();
                })
                .toList();

        // Step 3: Set the order details and update total
        order.setOrderDetails(orderDetails);
        double total = orderDetails.stream()
                .mapToDouble(od -> od.getBuyPrice() * od.getBuyQuantity())
                .sum();
        order.setTotal(total);

        // Step 4: Save the order (this will also save the order details)
        orderRepository.save(order);
    }

}
