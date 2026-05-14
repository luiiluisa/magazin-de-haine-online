package com.example.clothingstore.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private String category;
    private String details;
    private BigDecimal price;
    private Integer stock;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
