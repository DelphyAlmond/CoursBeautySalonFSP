package com.beautysalonbeugly.marketplace_backend.Repos;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beautysalonbeugly.marketplace_backend.Entities.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByOrderId(Long orderId);

    Optional<Receipt> findByVisitId(Long visitId);

    List<Receipt> findByReceiptDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Report purpose :
    // > total sum of all finalized transactions (receipts)
    // handled by a worker (through Receipt)
    // [ * ] This query might be better expressed using Criteria API in a
    // service or as two separate queries + muliple repo-s.

    @Query("SELECT SUM(r.finalTotalAmount) FROM Receipt r " +
            "WHERE r.worker.id = :workerId " +
            "AND r.receiptDate BETWEEN :startDate AND :endDate")
    Optional<Double> sumSells(Long workerId, LocalDateTime startDate, LocalDateTime endDate);

    // > count of items clarified by worker
    @Query("SELECT COUNT(r) FROM Receipt r " +
            "WHERE r.worker.id = :workerId " +
            "AND r.receiptDate BETWEEN :startDate AND :endDate")
    Long countReceipts(Long workerId, LocalDateTime startDate, LocalDateTime endDate);
}

// > total count of items (products + services) associated with receipts
// for a worker -> require complex aggregation across OrderItems and VisitItems.
// [ * ] Better to perform this aggregation in the service layer
// by calling sumProductSalesByWorker and sumServiceSalesByWorker from
// WorkerRepository.

// 1. Find all receipts by worker and date range.
// 2. For each receipt, if it has an Order, get its OrderItems.
// 3. For each receipt, if it has a Visit, get its VisitItems.
// 4. Sum quantities from all collected OrderItems and VisitItems.
// This cannot be efficiently done in a single JpaRepository method due to the
// multiple branching relationships.
