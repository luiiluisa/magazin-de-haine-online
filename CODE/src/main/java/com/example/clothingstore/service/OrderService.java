package com.example.clothingstore.service;

import com.example.clothingstore.model.*;
import com.example.clothingstore.repository.CartItemRepository;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.repository.ProductRepository;
import com.example.clothingstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartItemRepository cartItemRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(Long userId) {
        User consumer = findConsumer(userId);
        List<CartItem> cartItems = cartItemRepository.findByConsumer(consumer);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
        }

        Order order = new Order(consumer, LocalDateTime.now(), OrderStatus.NEW);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), product.getPrice());
            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByConsumer(consumer);

        return savedOrder;
    }

    public List<Order> findOrdersForConsumer(Long userId) {
        User consumer = findConsumer(userId);
        return orderRepository.findByConsumerOrderByCreatedAtDesc(consumer);
    }

    public List<Order> findAllOrdersForAdmin() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    private User findConsumer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.CONSUMER) {
            throw new RuntimeException("Only consumers can place orders");
        }

        return user;
    }
}
