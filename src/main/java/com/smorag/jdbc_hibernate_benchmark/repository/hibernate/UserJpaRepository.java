package com.smorag.jdbc_hibernate_benchmark.repository.hibernate;

import com.smorag.jdbc_hibernate_benchmark.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u
        SET u.firstName = :firstName
    """)
    int updateAllFirstNames(@Param("firstName") String firstName);

}