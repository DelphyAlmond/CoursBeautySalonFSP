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

    List<Shift> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}