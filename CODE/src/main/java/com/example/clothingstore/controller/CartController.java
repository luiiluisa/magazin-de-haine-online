package com.example.clothingstore.controller;

import com.example.clothingstore.dto.CartRequest;
import com.example.clothingstore.model.CartItem;
import com.example.clothingstore.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public List<CartItem> findCart(@PathVariable Long userId) {
        return cartService.findCart(userId);
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
    }

    @PutMapping("/{cartItemId}")
    public CartItem updateQuantity(@PathVariable Long cartItemId, @RequestBody CartRequest request) {
        return cartService.updateQuantity(cartItemId, request.getQuantity());
    }

    @DeleteMapping("/{cartItemId}")
    public void removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
    }
}
