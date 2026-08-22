package com.smorag.jdbc_hibernate_benchmark.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private Order order;
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private Product product;
    @Column(name="quantity")
    private int quantity;
    @Column(name="unit_price")
    private BigDecimal unitPrice;
}
