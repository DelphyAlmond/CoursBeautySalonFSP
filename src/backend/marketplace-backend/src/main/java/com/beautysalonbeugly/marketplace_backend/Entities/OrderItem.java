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
    private int quantity;
    private double priceAtOrder;
    private double discountPercentage;
    private double finalItemPrice;
    // [*] (priceAtOrder * quantity) * (1 - discountPercentage)

    // Order -<- = order item = -<- product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    @PreUpdate
    public void calculateFinalItemPrice() {
        double actualDiscount = Math.max(0.0, Math.min(1.0, this.discountPercentage));
        this.finalItemPrice = (this.priceAtOrder * this.quantity) * (1 - actualDiscount);
    }
}
