package com.example.clothingstore.config;

import com.example.clothingstore.model.Product;
import com.example.clothingstore.model.Role;
import com.example.clothingstore.model.User;
import com.example.clothingstore.repository.ProductRepository;
import com.example.clothingstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
        createUserIfMissing("Admin User", "admin", "123", Role.ADMIN);

        createUserIfMissing("Consumer User", "consumer", "123", Role.CONSUMER);
        createUserIfMissing("Consumer One", "consumer1", "123", Role.CONSUMER);
        createUserIfMissing("Consumer Two", "consumer2", "123", Role.CONSUMER);
        createUserIfMissing("Consumer Three", "consumer3", "123", Role.CONSUMER);
        createUserIfMissing("Consumer Four", "consumer4", "123", Role.CONSUMER);
        createUserIfMissing("Consumer Five", "consumer5", "123", Role.CONSUMER);

        if (productRepository.count() == 0) {
            productRepository.save(new Product(
                    "White T-Shirt",
                    "T-Shirts",
                    "Basic white cotton t-shirt",
                    new BigDecimal("49.99"),
                    20
            ));

            productRepository.save(new Product(
                    "Blue Jeans",
                    "Jeans",
                    "Regular fit blue jeans",
                    new BigDecimal("149.99"),
                    12
            ));

            productRepository.save(new Product(
                    "Black Hoodie",
                    "Hoodies",
                    "Warm black hoodie",
                    new BigDecimal("129.99"),
                    15
            ));

            productRepository.save(new Product(
                    "Summer Dress",
                    "Dresses",
                    "Light summer dress",
                    new BigDecimal("179.99"),
                    8
            ));
        }
    }

    private void createUserIfMissing(String fullName, String username, String password, Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User(
                    fullName,
                    username,
                    passwordEncoder.encode(password),
                    role
            );

            userRepository.save(user);
        }
    }
}