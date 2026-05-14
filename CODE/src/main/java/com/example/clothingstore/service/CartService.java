package com.example.clothingstore.service;

import com.example.clothingstore.model.CartItem;
import com.example.clothingstore.model.Product;
import com.example.clothingstore.model.Role;
import com.example.clothingstore.model.User;
import com.example.clothingstore.repository.CartItemRepository;
import com.example.clothingstore.repository.ProductRepository;
import com.example.clothingstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> findCart(Long userId) {
        User user = findConsumer(userId);
        return cartItemRepository.findByConsumer(user);
    }

    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        User user = findConsumer(userId);
        Product product = findProduct(productId);

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        CartItem item = cartItemRepository.findByConsumerAndProduct(user, product)
                .orElse(new CartItem(user, product, 0));

        int newQuantity = item.getQuantity() + quantity;

        if (product.getStock() < newQuantity) {
            throw new RuntimeException("Not enough stock");
        }

        item.setQuantity(newQuantity);
        return cartItemRepository.save(item);
    }

    public CartItem updateQuantity(Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        if (item.getProduct().getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void removeFromCart(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByConsumer(user);
    }

    private User findConsumer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.CONSUMER) {
            throw new RuntimeException("Only consumers can use the cart");
        }

        return user;
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
