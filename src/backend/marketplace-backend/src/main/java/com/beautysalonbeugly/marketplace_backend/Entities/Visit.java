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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitItem> visitItems = new ArrayList<>();

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Receipt receipt;

    private LocalDateTime visitDate;
    private boolean status = false;
    private double totalAmount;

    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        this.totalAmount = visitItems.stream()
                .mapToDouble(VisitItem::getFinalServicePrice)
                .sum();
    }

    public void addVisitService(VisitItem visitService) {
        visitItems.add(visitService);
        visitService.setVisit(this); // > Set the 'visit' field in the VisitItem
        calculateTotalAmount(); // > Recalculate total when service is added
    }

    public void removeVisitService(VisitItem visitService) {
        visitItems.remove(visitService);
        visitService.setVisit(null); // > Clear
        calculateTotalAmount(); // > Recalculate when removed
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
