package com.example.clothingstore.repository;

import com.example.clothingstore.model.CartItem;
import com.example.clothingstore.model.Product;
import com.example.clothingstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByConsumer(User consumer);
    Optional<CartItem> findByConsumerAndProduct(User consumer, Product product);
    void deleteByConsumer(User consumer);
}
