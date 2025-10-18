package com.beautysalonbeugly.marketplace_backend.Repos;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beautysalonbeugly.marketplace_backend.Entities.Worker;
import com.beautysalonbeugly.marketplace_backend.Enums.RoleType;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    List<Worker> findByRole(RoleType role);

    List<Worker> findByEmployed(boolean employed);

    Optional<Worker> findByNameAndSurname(String name, String surname);

    // > sum of product Order-sales by worker within a date range
    // BUT Worker is associated with the Order through Receipt [ ! ]

    // If 'Worker worker' field wd be in Order:
    /*
     * @Query("SELECT SUM(oi.finalItemPrice) FROM Order o JOIN o.orderItems oi WHERE
     * o.worker.id = :workerId AND o.placeDate BETWEEN :startDate AND :endDate")
     * Double sumProductSalesByWorkerAndDateBetween(Long workerId, LocalDateTime
     * startDate, LocalDateTime endDate);
     */

    // > sum of service/Visit-sales by worker (master) within a date range
    @Query("SELECT SUM(vi.finalServicePrice) FROM Visit v JOIN v.visitItems vi WHERE v.worker.id = :workerId AND v.visitDate BETWEEN :startDate AND :endDate")
    Double sumServiceSales(Long workerId, LocalDateTime startDate, LocalDateTime endDate);

    // > count of visits performed by a worker within a date range : report p.-se
    // only
    @Query("SELECT COUNT(v) FROM Visit v WHERE v.worker.id = :workerId AND v.visitDate BETWEEN :startDate AND :endDate")
    Long countVisitsByWorker(Long workerId, LocalDateTime startDate, LocalDateTime endDate);
}