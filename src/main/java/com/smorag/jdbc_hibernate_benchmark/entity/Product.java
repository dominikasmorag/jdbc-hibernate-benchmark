package com.smorag.jdbc_hibernate_benchmark.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Table(name="products")
@Entity
public class Product {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="category")
    private String category;
    @Column(name="price")
    private BigDecimal price;
    @Column(name="stock_quantity")
    private int stockQuantity;
    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
