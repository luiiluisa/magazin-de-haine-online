package com.example.clothingstore.service;

import com.example.clothingstore.dto.ProductRequest;
import com.example.clothingstore.model.Product;
import com.example.clothingstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product create(ProductRequest request) {
        validateProduct(request);

        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getDetails(),
                request.getPrice(),
                request.getStock()
        );

        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        validateProduct(request);

        Product product = findById(id);
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDetails(request.getDetails());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }

    private void validateProduct(ProductRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Product name is required");
        }

        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new RuntimeException("Product category is required");
        }

        if (request.getPrice() == null || request.getPrice().doubleValue() <= 0) {
            throw new RuntimeException("Product price must be positive");
        }

        if (request.getStock() == null || request.getStock() < 0) {
            throw new RuntimeException("Product stock must be positive or zero");
        }
    }
}
