package com.beautysalonbeugly.marketplace_backend.Repos;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beautysalonbeugly.marketplace_backend.Entities.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByWorkerId(Long workerId);

    @Query("SELECT s FROM Shift s WHERE s.worker.id = :workerId AND s.startTime <= :currentTime AND s.endTime >= :currentTime AND s.active = true")
    Optional<Shift> findActiveShift(Long workerId, LocalDateTime currentTime);

    List<Shift> findByDateGap(LocalDateTime start, LocalDateTime end);

    // for reporting :
    // 1. Count of shifts for a worker within a date range
    Long countByDateGap(Long workerId, LocalDateTime startDate, LocalDateTime endDate);

    // 2. Sum of shift durations for a worker within a date range (in hours)
    // > sum the difference between endTime and startTime in seconds,
    // then convert to hours.
    @Query("SELECT SUM(FUNCTION('EXTRACT', 'EPOCH' FROM (s.endTime - s.startTime))) / 3600.0 " +
            "FROM Shift s " +
            "WHERE s.worker.id = :workerId AND s.startTime BETWEEN :startDate AND :endDate")
    Optional<Double> sumShiftDuration(Long workerId, LocalDateTime startDate,
            LocalDateTime endDate);
}