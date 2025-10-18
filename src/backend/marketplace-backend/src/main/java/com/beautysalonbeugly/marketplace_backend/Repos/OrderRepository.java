package com.beautysalonbeugly.marketplace_backend.Repos;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beautysalonbeugly.marketplace_backend.Entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // > date range filter
    List<Order> findOrdersByDate(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findOrderByCustomer(Long customerId);
}