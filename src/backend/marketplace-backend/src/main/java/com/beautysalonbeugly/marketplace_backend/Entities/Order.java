package com.beautysalonbeugly.marketplace_backend.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "order_pack") // [!] Avoid conflict with SQL keyword 'ORDER'
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(exclude = { "orderItems", "customer", "receipt" })

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // Initialize to prevent NullPointerExceptions

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Receipt receipt;

    private LocalDateTime placeDate;
    private boolean status = false;
    private double totalAmount;

    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getFinalItemPrice)
                .sum();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); // > Set the 'order' field in the OrderItem
        calculateTotalAmount(); // > Recalculate total when item is added
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null); // > Clear
        calculateTotalAmount(); // > Recalculate
    }

    public void setReceipt(Receipt receipt) {
        if (receipt == null) {
            if (this.receipt != null) {
                this.receipt.setOrder(null); // > Disassociate old receipt
            }
        } else {
            receipt.setOrder(this); // > Associate new receipt
        }
        this.receipt = receipt;
    }
}