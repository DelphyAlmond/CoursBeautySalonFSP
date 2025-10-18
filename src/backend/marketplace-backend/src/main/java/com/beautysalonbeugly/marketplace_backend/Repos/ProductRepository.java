package com.beautysalonbeugly.marketplace_backend.Repos;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.beautysalonbeugly.marketplace_backend.Entities.Product;

// the first layer to talk to data base prim-ly [ ! ]
// + all JPA @Entity classes will have a corresponding Spring Data JPA @Repository interface [ * ]

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByArticl(String art);
}
