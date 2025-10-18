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
    // handled by a worker (via their associated Order/Visit)
    // [ ! ] Receipt linked to EITHER an Order/Visit -> fetch worker from
    // (Joining through the Order/Visit and then to Worker)

    // So = if a Receipt has an Order, the worker for the order
    // is relevant, else: the worker for the visit is relevant.

    // [ * ] this query might be better expressed using Criteria API in a
    // service or as two separate queries + muliple repo-s.

    @Query("SELECT SUM(r.finalTotalAmount) FROM Receipt r " +
            "LEFT JOIN r.order o " +
            "LEFT JOIN r.visit v " +
            "WHERE (o.worker.id = :workerId OR v.worker.id = :workerId) " +
            "AND r.receiptDate BETWEEN :startDate AND :endDate")
    Optional<Double> sumFinalTotalAmountByWorkerAndDateRange(Long workerId, LocalDateTime startDate,
            LocalDateTime endDate);

    // > total count of items (products + services) associated with receipts for a
    // worker
    // This would require more complex aggregation across OrderItems and VisitItems.
    // It's generally better to perform this aggregation in the service layer
    // by calling sumProductSalesByWorker and sumServiceSalesByWorker from
    // WorkerRepository.
    // Or, if Order and Visit had direct worker links, a query could be made more
    // complex.
    // For now, let's stick to total transaction amount.
}