package com.smorag.jdbc_hibernate_benchmark.repository.hibernate;

import com.smorag.jdbc_hibernate_benchmark.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
