package com.dev.pricetracker.repository;

import com.dev.pricetracker.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Essa interface já vem com os métodos save(), findAll(), deleteById(), etc.
}