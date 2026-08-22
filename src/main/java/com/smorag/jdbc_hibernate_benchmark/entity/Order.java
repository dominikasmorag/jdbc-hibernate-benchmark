package com.smorag.jdbc_hibernate_benchmark.entity;

import enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @ManyToOne
//    @JoinColumn(name = "user_id")
    // domyślnie fetch Type Eager
    private User user;
    @Column(name="total_amount")
    private BigDecimal totalAmount;
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
