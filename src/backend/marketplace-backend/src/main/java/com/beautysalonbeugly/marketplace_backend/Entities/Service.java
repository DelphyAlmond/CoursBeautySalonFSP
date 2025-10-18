package com.beautysalonbeugly.marketplace_backend.Entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "salon_service")
// [!] 'salon_service' to avoid conflict with potential SQL keyword 'SERVICE'
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(exclude = { "visitItems" })

public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String cover; // URL or link to the image for the service
    private String shortInfo;
    @Column(columnDefinition = "TEXT")
    // TEXT for potentially longer descriptions [!]
    private String description;
    private double price;
    private boolean approved = false;

    // [*] One service can be part of many visit-servs entries (in different visits)
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<VisitItem> visitItems = new java.util.ArrayList<>();

    public void addVisitService(VisitItem visitItem) {
        visitItems.add(visitItem);
        visitItem.setService(this);
    }

    public void removeVisitService(VisitItem visitItem) {
        visitItems.remove(visitItem);
        visitItem.setService(null);
    }
}