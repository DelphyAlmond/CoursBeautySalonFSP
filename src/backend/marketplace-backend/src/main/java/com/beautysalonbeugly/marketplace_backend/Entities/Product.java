package com.beautysalonbeugly.marketplace_backend.Entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;

import com.beautysalonbeugly.marketplace_backend.Enums.ProductType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(exclude = { "orderItems" })

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String articl;

    private String title;
    private String cover;
    // ^ URL or link to the image for the product

    @Column(name = "short_info", length = 500)
    private String shortInfo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double price;

    @Column(columnDefinition = "TEXT")
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 50)
    private ProductType productType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<OrderItem> orderItems = new java.util.ArrayList<>();

    // Helper method to manage bidirectional relationship

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setProduct(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setProduct(null);
    }

    // Constructor without ID and articl for creating new products
    public Product(String title, String cover, String shortInfo, String description,
            Double price, Integer quantity, ProductType productType, String articl) {
        this.title = title;
        this.cover = cover;
        this.shortInfo = shortInfo;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.productType = productType;
        this.articl = articl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", articl='" + articl + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", shortInfo='" + shortInfo + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", productType='" + productType + '\'' +
                '}';
    }
}