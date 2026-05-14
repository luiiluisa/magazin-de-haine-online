package com.example.clothingstore.controller;

import com.example.clothingstore.dto.PlaceOrderRequest;
import com.example.clothingstore.model.Order;
import com.example.clothingstore.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public Order placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request.getUserId());
    }

    @GetMapping("/consumer/{userId}")
    public List<Order> findOrdersForConsumer(@PathVariable Long userId) {
        return orderService.findOrdersForConsumer(userId);
    }

    @GetMapping("/admin")
    public List<Order> findAllOrdersForAdmin() {
        return orderService.findAllOrdersForAdmin();
    }

    @PutMapping("/{orderId}/confirm")
    public Order confirmOrder(@PathVariable Long orderId) {
        return orderService.confirmOrder(orderId);
    }
}
