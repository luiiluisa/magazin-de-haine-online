package com.example.clothingstore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User consumer;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public CartItem() {
    }

    public CartItem(User consumer, Product product, Integer quantity) {
        this.consumer = consumer;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public User getConsumer() {
        return consumer;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConsumer(User consumer) {
        this.consumer = consumer;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
