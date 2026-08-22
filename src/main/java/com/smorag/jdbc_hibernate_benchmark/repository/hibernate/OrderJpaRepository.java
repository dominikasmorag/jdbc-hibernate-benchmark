package com.smorag.jdbc_hibernate_benchmark.repository.hibernate;

import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.user")
    List<Order> findAllWithUser();

    @Transactional
    @Modifying
    @Query("""
    UPDATE Order o
    SET o.status = :status
    """)
    int updateAllStatuses(@Param("status") OrderStatus status);
}

