package com.beautysalonbeugly.marketplace_backend.Entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "customer")
@Data // [*] Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // - no-argument constructor
@AllArgsConstructor // - constructor with all fields

// [*] Exclude collections and parent/child relationships from equals/hashCode
@EqualsAndHashCode(exclude = { "orders", "visits" })

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // [ * ] @Column(table = '...') при связи с таблицой извне, в случае несоответствия текущему набору атрибутов для объекта\структуры
    private String nick;
    private String email;
    private String phoneNumber;
    private int loyaltyPoints; // > can determine discount tier

    // customer - many orders
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Order> orders = new java.util.ArrayList<>();

    // customer - many visits
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Visit> visits = new java.util.ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        /*
         * if (!order.get().contains(this)) { // != this
         * order.set() / set(this) ссылка на 1ю сущность из другой, доб. ссылку на др в 1ю:
         * оба ссылаются друг на друга *(добав. в коллекцию/ изм. атрибута) - иначе невыполенеие ограничений
         * }
         */
        order.setCustomer(this);
    }

    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setCustomer(this);
    }
}
