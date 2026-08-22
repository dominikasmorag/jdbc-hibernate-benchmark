package com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc;

import com.smorag.jdbc_hibernate_benchmark.BenchmarkData;
import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import com.smorag.jdbc_hibernate_benchmark.repository.jdbc.OrderJdbcRepository;
import com.smorag.jdbc_hibernate_benchmark.repository.jdbc.UserJdbcRepository;
import com.smorag.jdbc_hibernate_benchmark.utils.CsvWriter;
import com.smorag.jdbc_hibernate_benchmark.utils.DataSeeder;
import com.smorag.jdbc_hibernate_benchmark.utils.DatabaseCleaner;
import enums.OrderStatus;
import enums.QueryType;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcOrderBenchmark extends JdbcBaseBenchmark {
    private final OrderJdbcRepository orderRepository;
    private final UserJdbcRepository userJdbcRepository;
    private final static String ENTITY_NAME = "Order";
    private final static String REPORT_FILE_NAME = ENTITY_NAME + DAY + MONTH + ".csv";
    private final CsvWriter csvWriter;

    public JdbcOrderBenchmark(OrderJdbcRepository orderRepository, DataSource dataSource, UserJdbcRepository userJdbcRepository) {
        super(dataSource);
        this.orderRepository = orderRepository;
        this.csvWriter = new CsvWriter(REPORT_FILE_NAME);
        this.userJdbcRepository = userJdbcRepository;
    }

    public void saveAll(List<Order> orderList, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            String method = "INSERT";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);
            }
            for (int i = 0; i < iterationQuantity; i++) {
                List<Order> copyOrders = BenchmarkData.cloneOrders(orderList);
                int[] assignments = DataSeeder.ORDER_ASSIGNMENTS.get(orderList);
                System.out.println("Assignments length " + assignments.length);
                DataSeeder.seedAndSaveToDb(userJdbcRepository, DataSeeder.map.get(orderList));
                List<User> usersFromDb = userJdbcRepository.findAll();
                for (int j = 0; j < orderList.size(); j++) {
                    copyOrders.get(j).setUser(usersFromDb.get(assignments[j]));
                }
                System.out.println("JDBC saveAll(orderList)");
                if(measureTime) {
                    long start = System.currentTimeMillis();
                    orderRepository.saveAll(copyOrders);
                    long finish = System.currentTimeMillis() - start;
                    System.out.print((i + 1) + ". " + finish + "ms |");

                    csvWriter.appendRow((i + 1), FRAMEWORK, ENTITY_NAME, QueryType.INSERT, orderList.size(), finish, "INSERT INTO orders (user_id total_amount status) VALUES (? ? ?)");
                    DatabaseCleaner.clearDatabase(dataSource);
                }
            }
    }

    public List<Order> findAll(List<Order> orderList, int iterationQuantity, boolean measureTime) {
        List<Order> ordersFromDb = new ArrayList<>();
        if(measureTime) {
            String method = "SELECT";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);
        }
        for (int i = 0; i < iterationQuantity; i++) {
            saveAll(orderList, 1, false);
            long start = System.currentTimeMillis();
            ordersFromDb = orderRepository.findAll();
            long finish = System.currentTimeMillis() - start;
            if(measureTime) {
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i + 1), FRAMEWORK, ENTITY_NAME, QueryType.SELECT, orderList.size(), finish, "SELECT * FROM ORDERS");
                DatabaseCleaner.clearDatabase(dataSource);
            }
        }
        return ordersFromDb;
    }

    public void updateAllStatuses(List<Order> orderList, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            String method = "UPDATE";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: " + method + "\n----------------------");

            csvWriter.initColumnNames(COLUMN_NAMES);
            saveAll(orderList, 1, false);
        }
        for (int i = 0; i < iterationQuantity; i++) {
            long start = System.currentTimeMillis();
            orderRepository.updateAllStatuses(OrderStatus.COMPLETED);
            long finish = System.currentTimeMillis() - start;
            if(measureTime) {
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i + 1), FRAMEWORK, ENTITY_NAME, QueryType.UPDATE, orderList.size(), finish, "UPDATE orders SET status = ?");
            }
        }
        DatabaseCleaner.clearDatabase(dataSource);
    }

    public void deleteAll(List<Order> orderList, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            String method = "DELETE";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: " + method + "\n----------------------");

            csvWriter.initColumnNames(COLUMN_NAMES);
        }
        for (int i = 0; i < iterationQuantity; i++) {
            saveAll(orderList, 1, false);
            long start = System.currentTimeMillis();
            orderRepository.deleteAll();
            long finish = System.currentTimeMillis() - start;
            if(measureTime) {
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i + 1), FRAMEWORK, ENTITY_NAME, QueryType.DELETE, orderList.size(), finish, "DELETE FROM ORDERS");
                DatabaseCleaner.clearDatabase(dataSource);
            }
        }
    }

}
