package com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate;

import com.smorag.jdbc_hibernate_benchmark.entity.Product;
import com.smorag.jdbc_hibernate_benchmark.repository.hibernate.ProductJpaRepository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class HibernateProductBenchmark extends HibernateBaseBenchmark {
    private final ProductJpaRepository productJpaRepository;

    public HibernateProductBenchmark(ProductJpaRepository productJpaRepository, DataSource dataSource) {
        super(dataSource);
        this.productJpaRepository = productJpaRepository;
    }

    public void saveAll(List<Product> products) {
        productJpaRepository.saveAll(products);
    }
}
