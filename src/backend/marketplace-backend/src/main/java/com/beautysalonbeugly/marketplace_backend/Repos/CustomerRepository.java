package com.beautysalonbeugly.marketplace_backend.Repos;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beautysalonbeugly.marketplace_backend.Entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByNick(String nick);

    // > just for count orders(item list) : report purpose only
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.id = :customerId")
    Long countOrdersByCustomerId(Long customerId);

    // > get a customer along with their orders
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.id = :customerId")
    Optional<Customer> findByIdWithOrders(Long customerId);

    // > get a customer along with their visits
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.visits WHERE c.id = :customerId")
    Optional<Customer> findByIdWithVisits(Long customerId);
}