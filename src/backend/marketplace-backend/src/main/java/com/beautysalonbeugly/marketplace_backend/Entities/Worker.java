package com.beautysalonbeugly.marketplace_backend.Entities;

import com.beautysalonbeugly.marketplace_backend.Enums.RoleType;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "worker")
@Data
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(exclude = { "visits" })

public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private boolean employed = true;

    @Enumerated(EnumType.STRING)
    // > Store enum name as a String in the database
    private RoleType role;

    // worker - many visits
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Visit> visits = new java.util.ArrayList<>();

    // 2 methods to manage bidirectional relationship [!]
    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setWorker(this);
    }

    public void removeVisit(Visit visit) {
        visits.remove(visit);
        visit.setWorker(null);
    }
}
