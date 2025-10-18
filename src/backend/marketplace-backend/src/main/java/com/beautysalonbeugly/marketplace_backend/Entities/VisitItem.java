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
    private int quantityOrDuration;
    private double priceAtVisit;
    private double discountPercentage;
    private double finalServicePrice;

    // Visit -<- = visit item = -<- service

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private SalonService service;

    @PrePersist
    @PreUpdate
    public void calculateFinalServicePrice() {
        double actualDiscount = Math.max(0.0, Math.min(1.0, this.discountPercentage));
        this.finalServicePrice = (this.priceAtVisit * this.quantityOrDuration) * (1 - actualDiscount);
    }
}
