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
@Table(name = "visit_item")
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(exclude = { "visit", "service" })

public class VisitItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // > Represent duration (e.g., 60 minutes). Adjust as per business logic.
    private int quantityOrDuration;
    private double priceAtVisit; // The price of ONE service unit/duration at the moment of the visit
    private double discountPercentage;
    private double finalServicePrice;

    // >> Many VisitServices belong to one Visit
    // The foreign key 'visit_id' will be created in the 'visit_service' table [!]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    // >> Many VisitServices refer to one Service
    // The foreign key 'service_id' will be created in the 'visit_service' table [!]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @PrePersist
    @PreUpdate
    public void calculateFinalServicePrice() {
        // Ensure discountPercentage is within a valid range (0.0 to 1.0)
        double actualDiscount = Math.max(0.0, Math.min(1.0, this.discountPercentage));
        this.finalServicePrice = (this.priceAtVisit * this.quantityOrDuration) * (1 - actualDiscount);
    }
}
