package com.example.clothingstore.repository;

import com.example.clothingstore.model.Order;
import com.example.clothingstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByConsumerOrderByCreatedAtDesc(User consumer);
    List<Order> findAllByOrderByCreatedAtDesc();
}
