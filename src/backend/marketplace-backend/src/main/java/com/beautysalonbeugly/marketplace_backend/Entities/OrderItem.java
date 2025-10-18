package com.beautysalonbeugly.marketplace_backend.Entities;

import lombok.Data;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor

// [!] For JPA entities with bidirectional relationships,
// exclude related entities from equals/hashCode
// to prevent infinite loops and performance issues.
// Use only primitive or non-relational fields for identity.
@EqualsAndHashCode(exclude = { "order", "product" })

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity; // > How many units of this product are in the current list
    private double priceAtOrder; // > The price of product UNIT
    private double discountPercentage; // discount applied to item (* 0.10 for 10% off)
    private double finalItemPrice; // [*] (priceAtOrder * quantity) * (1 - discountPercentage)

    // >> Many OrderItems belong to one Order
    // The foreign key 'order_id' will be created in the 'order_item' table [!]
    @ManyToOne(fetch = FetchType.LAZY) // Efficient: Don't load the whole Order unless needed [!]
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    // >> Many OrderItems refer to one Product
    // The foreign key 'product_id' will be created in the 'order_item' table [!]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // [*] Method will be called automatically by JPA/Hibernate,
    // before an entity is first PERSISTED (inserted) into the DB.
    @PrePersist
    // [*] + method will be called automatically
    // before an existing entity is UPDATED in the DB.
    @PreUpdate
    public void calculateFinalItemPrice() {
        // Ensure discountPercentage is within a valid range (0.0 to 1.0)
        double actualDiscount = Math.max(0.0, Math.min(1.0, this.discountPercentage));
        this.finalItemPrice = (this.priceAtOrder * this.quantity) * (1 - actualDiscount);
    }
}