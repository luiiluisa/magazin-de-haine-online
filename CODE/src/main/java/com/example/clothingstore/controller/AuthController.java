package com.example.clothingstore.controller;

import com.example.clothingstore.dto.LoginRequest;
import com.example.clothingstore.dto.UserResponse;
import com.example.clothingstore.model.User;
import com.example.clothingstore.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        return new UserResponse(user);
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logged out successfully";
    }
}
