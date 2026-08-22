package com.smorag.jdbc_hibernate_benchmark.utils;

import com.smorag.jdbc_hibernate_benchmark.BenchmarkData;
import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import com.smorag.jdbc_hibernate_benchmark.repository.jdbc.UserJdbcRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataSeeder {


    public static Map<List<Order>, List<User>> map = initOrderUserMap();
    public static final Map<List<Order>, int[]> ORDER_ASSIGNMENTS = new HashMap<>();

    public static List seedAndSaveToDb(JpaRepository jpaRepository, List<User> users) {
        List<User> copyUsers = BenchmarkData.cloneUsers(users);
        return jpaRepository.saveAll(copyUsers);
    }

    public static List seedAndSaveToDb(UserJdbcRepository userJdbcRepository, List<User> users) {
        List<User> copyUsers = BenchmarkData.cloneUsers(users);
        return userJdbcRepository.saveAll(copyUsers);
    }

    static {

        ORDER_ASSIGNMENTS.put(
                BenchmarkData.ORDERS_10K,
                AssignmentGenerator.generateAssignments(
                        BenchmarkData.ORDERS_10K.size(),
                        BenchmarkData.REFERENCE_USERS_100.size()
                )
        );

        ORDER_ASSIGNMENTS.put(
                BenchmarkData.ORDERS_50K,
                AssignmentGenerator.generateAssignments(
                        BenchmarkData.ORDERS_50K.size(),
                        BenchmarkData.REFERENCE_USERS_5K.size()
                )
        );

        ORDER_ASSIGNMENTS.put(
                BenchmarkData.ORDERS_100K,
                AssignmentGenerator.generateAssignments(
                        BenchmarkData.ORDERS_100K.size(),
                        BenchmarkData.REFERENCE_USERS_10K.size()
                )
        );
    }

    private static Map<List<Order>, List<User>> initOrderUserMap() {
        Map<List<Order>, List<User>> map = new HashMap<>();
        map.put(BenchmarkData.ORDERS_10K, BenchmarkData.REFERENCE_USERS_100);
        map.put(BenchmarkData.ORDERS_50K, BenchmarkData.REFERENCE_USERS_5K);
        map.put(BenchmarkData.ORDERS_100K, BenchmarkData.REFERENCE_USERS_10K);
        return map;
    }

}
