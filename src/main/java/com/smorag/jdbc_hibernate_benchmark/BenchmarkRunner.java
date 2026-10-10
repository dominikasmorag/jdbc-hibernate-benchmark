package com.smorag.jdbc_hibernate_benchmark;

import com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate.HibernateOrderBenchmark;
import com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate.HibernateUserBenchmark;
import com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc.JdbcOrderBenchmark;
import com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc.JdbcUserBenchmark;
import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

// wu - załączniki - badania, excel
// demo podczas obrony - filmik z zainicjalizowaniem funkcji pomiaru czasów -> wyniki rezultatów -> wykresy ->załączniki - w ramach załączników lin do zew. repozytorium  (drive)
@Component
public class BenchmarkRunner {

    public BenchmarkRunner() {

    }

    public void startUserBenchmark(JdbcUserBenchmark jdbcUserBenchmark, HibernateUserBenchmark hibernateUserBenchmark, int iterationQuantity) {
        startUserBenchmarkWithData(BenchmarkData.USERS_10K, jdbcUserBenchmark, hibernateUserBenchmark, iterationQuantity);
        startUserBenchmarkWithData(BenchmarkData.USERS_50K, jdbcUserBenchmark, hibernateUserBenchmark, iterationQuantity);
        startUserBenchmarkWithData(BenchmarkData.USERS_100K, jdbcUserBenchmark, hibernateUserBenchmark, iterationQuantity);
    }

    public void startOrderBenchmark(JdbcOrderBenchmark jdbcOrderBenchmark, HibernateOrderBenchmark hibernateOrderBenchmark, int iterationQuantity) {
        startOrderBenchmarkWithData(BenchmarkData.ORDERS_10K, jdbcOrderBenchmark, hibernateOrderBenchmark, iterationQuantity);
        startOrderBenchmarkWithData(BenchmarkData.ORDERS_50K, jdbcOrderBenchmark, hibernateOrderBenchmark, iterationQuantity);
        startOrderBenchmarkWithData(BenchmarkData.ORDERS_100K, jdbcOrderBenchmark, hibernateOrderBenchmark, iterationQuantity);
    }

    private void startUserBenchmarkWithData(List<User> users, JdbcUserBenchmark jdbcUserBenchmark, HibernateUserBenchmark hibernateUserBenchmark, int iterationQuantity) {
        System.out.println("==============================================\nStarting User benchmark, records size: " + users.size() + "\n==============================================");

        jdbcUserBenchmark.saveAll(users, iterationQuantity, true);
        hibernateUserBenchmark.saveAll(users, iterationQuantity, true);

        jdbcUserBenchmark.findAll(users, iterationQuantity, true);
        hibernateUserBenchmark.findAll(users, iterationQuantity, true);

        jdbcUserBenchmark.updateAllFirstNames(users, iterationQuantity, true);
        hibernateUserBenchmark.updateAllFirstNames(users, iterationQuantity, true);

        jdbcUserBenchmark.deleteUsers(users, iterationQuantity, true);
        hibernateUserBenchmark.deleteAll(users, iterationQuantity, true, false);
    }

    private void startOrderBenchmarkWithData(List<Order> orders, JdbcOrderBenchmark jdbcOrderBenchmark, HibernateOrderBenchmark hibernateOrderBenchmark, int iterationQuantity) {
        System.out.println("==============================================\nStarting Order benchmark, records size: " + orders.size() + "\n==============================================");

        jdbcOrderBenchmark.saveAll(orders, iterationQuantity, true);
        hibernateOrderBenchmark.saveAll(orders, iterationQuantity, true);

        jdbcOrderBenchmark.findAll(orders, iterationQuantity, true);
        hibernateOrderBenchmark.findAll(orders, iterationQuantity, true);

        jdbcOrderBenchmark.updateAllStatuses(orders, iterationQuantity, true);
        hibernateOrderBenchmark.updateAllStatuses(orders, iterationQuantity, true);

        jdbcOrderBenchmark.deleteAll(orders, iterationQuantity, true);
        hibernateOrderBenchmark.deleteAll(orders, iterationQuantity, true, false);
    }
}
