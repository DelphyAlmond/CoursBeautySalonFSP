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
@Table(name = "visit")
@Data
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(exclude = { "visitItems", "customer", "worker", "receipt" })

public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    private Long id;

    // >> Many Visits can belong to one Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // >> Many Visits can be performed by one Worker
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker; // [*] The worker who performed the services

    // >> One Visit can have Many VisitItems
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitItem> visitItems = new ArrayList<>(); // * Initialize to prevent NullPointerExceptions

    // >> A Visit can have one Receipt
    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Receipt receipt;

    private LocalDateTime visitDate;
    private boolean status = false;
    private double totalAmount;

    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        // * Sum up the final price of all visit services
        this.totalAmount = visitItems.stream()
                .mapToDouble(VisitItem::getFinalServicePrice)
                .sum();
    }

    // --- Helper Methods for Bidirectional Relationship Management ---
    // Good practice to keep both sides of a bidirectional relationship in sync

    public void addVisitService(VisitItem visitService) {
        visitItems.add(visitService);
        visitService.setVisit(this); // > Set the 'visit' field in the VisitService
        calculateTotalAmount(); // > Recalculate total when service is added
    }

    public void removeVisitService(VisitItem visitService) {
        visitItems.remove(visitService);
        visitService.setVisit(null); // > Clear the 'visit' field in the VisitService
        calculateTotalAmount(); // > Recalculate total when service is removed
    }

    public void setReceipt(Receipt receipt) {
        if (receipt == null) {
            if (this.receipt != null) {
                this.receipt.setVisit(null); // > Disassociate old receipt
            }
        } else {
            receipt.setVisit(this); // > Associate new receipt
        }
        this.receipt = receipt;
    }
}
