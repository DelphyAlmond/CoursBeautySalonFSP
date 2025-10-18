package com.beautysalonbeugly.marketplace_backend.Entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "receipt")
@Data
@AllArgsConstructor
@NoArgsConstructor

// Exclude parent/child from equals/hashCode
@EqualsAndHashCode(exclude = { "order", "visit" })

public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A Receipt is linked to exactly one Order (One-to-One relationship)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true) // Foreign key to Order table, must be unique
    private Order order;

    // A Receipt is linked to exactly one Visit (One-to-One relationship)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", unique = true) // Foreign key to Visit table, must be unique
    private Visit visit;

    private LocalDateTime receiptDate;
    private double finalTotalAmount; // The final amount after all calculations (Order.totalAmount +
                                     // Visit.totalAmount)

    @PrePersist
    @PreUpdate
    public void calculateFinalTotalAmount() {
        double orderTotal = (order != null) ? order.getTotalAmount() : 0.0;
        double visitTotal = (visit != null) ? visit.getTotalAmount() : 0.0;
        this.finalTotalAmount = orderTotal + visitTotal;
    }
}
