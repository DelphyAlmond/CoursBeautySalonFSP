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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "receipt")
@Data
@AllArgsConstructor
@NoArgsConstructor

// Exclude parent/child from equals/hashCode : + worker(id)
@EqualsAndHashCode(exclude = { "order", "visit", "worker" })

public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", unique = true)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    private LocalDateTime receiptDate;
    private double finalTotalAmount; // [ * ] (Order.totalAmount + Visit.totalAmount)

    @PrePersist
    @PreUpdate
    public void calculateFinalTotalAmount() {
        double orderTotal = (order != null) ? order.getTotalAmount() : 0.0;
        double visitTotal = (visit != null) ? visit.getTotalAmount() : 0.0;
        this.finalTotalAmount = orderTotal + visitTotal;
    }
}
