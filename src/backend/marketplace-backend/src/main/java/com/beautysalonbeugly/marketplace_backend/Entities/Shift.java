package com.beautysalonbeugly.marketplace_backend.Entities;

import java.time.LocalDateTime;

import lombok.Data;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType; // lazy loading
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "shift")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Proper ManyToOne relationship to Worker
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false) // Foreign key column
    private Worker worker; // Reference to the actual Worker entity

    private LocalDateTime startTime; // Use LocalDateTime
    private LocalDateTime endTime; // Use LocalDateTime

    // Optional: a flag to indicate if the shift is currently active
    private boolean active = true;

    // Constructor without ID, for creating new shifts
    public Shift(Worker worker, LocalDateTime startTime, LocalDateTime endTime) {
        this.worker = worker;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
