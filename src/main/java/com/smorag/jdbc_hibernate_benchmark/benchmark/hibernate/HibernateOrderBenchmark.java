package com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate;

import com.smorag.jdbc_hibernate_benchmark.BenchmarkData;
import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import com.smorag.jdbc_hibernate_benchmark.repository.hibernate.OrderJpaRepository;
import com.smorag.jdbc_hibernate_benchmark.repository.hibernate.UserJpaRepository;
import com.smorag.jdbc_hibernate_benchmark.utils.AssignmentGenerator;
import com.smorag.jdbc_hibernate_benchmark.utils.CsvWriter;
import com.smorag.jdbc_hibernate_benchmark.utils.DataSeeder;
import com.smorag.jdbc_hibernate_benchmark.utils.DatabaseCleaner;
import enums.OrderStatus;
import enums.QueryType;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class HibernateOrderBenchmark extends HibernateBaseBenchmark {
    private final OrderJpaRepository orderJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final static String ENTITY_NAME = "Order";
    private final static String REPORT_FILE_NAME = ENTITY_NAME + DAY + MONTH + ".csv";
    private final CsvWriter csvWriter;

    public HibernateOrderBenchmark(OrderJpaRepository orderJpaRepository, UserJpaRepository userJpaRepository, DataSource dataSource) {
        super(dataSource);
        this.orderJpaRepository = orderJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.csvWriter = new CsvWriter(REPORT_FILE_NAME);

    }

    public void saveAll(List<Order> orderList, int iterationQuantity, boolean measureTime) {

            int[] assignments = AssignmentGenerator.generateAssignments(orderList.size(), DataSeeder.map.get(orderList).size());
            if(measureTime) {
                System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: saveAll()\n----------------------");
                csvWriter.initColumnNames(COLUMN_NAMES);
            }
            for (int i = 0; i < iterationQuantity ; i++) {

                List<User> usersFromDb = DataSeeder.seedAndSaveToDb(userJpaRepository, DataSeeder.map.get(orderList));
                List<Order> copyOrders = BenchmarkData.cloneOrders(orderList);

                for (int j = 0; j < orderList.size() ; j++) {
                    copyOrders.get(j).setUser(usersFromDb.get(assignments[j]));
                }

                long start = System.currentTimeMillis();
                orderJpaRepository.saveAll(copyOrders);
                long finish = System.currentTimeMillis() - start;

                if(measureTime) {
                    System.out.print((i + 1) + ". " + finish + "ms |");
                    csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.INSERT, orderList.size(), finish, "saveAll()");
                    System.out.print((i + 1) + ". " + finish + "ms |");
                    DatabaseCleaner.clearDatabase(dataSource);
                }
                else {
                    return;
                }
            }
    }

    public List<Order> findAll(List<Order> orderList, int iterationQuantity, boolean measureTime) {
        List<Order> ordersFromDb = List.of();

        csvWriter.initColumnNames(COLUMN_NAMES);

        saveAll(orderList, 1, false);
        if(measureTime) {
            System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: findAll()\nFetch type: eager\n----------------------");

            for (int i = 0; i < iterationQuantity; i++) {
                long start = System.currentTimeMillis();
                ordersFromDb = orderJpaRepository.findAll();
                long finish = System.currentTimeMillis() - start;

                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.SELECT, orderList.size(), finish, "findAll() fetchType = eager");
                System.out.print((i + 1) + ". " + finish + "ms |");
            }
        }
        DatabaseCleaner.clearDatabase(dataSource);
        return ordersFromDb;
    }

    public List<Order> findAllWithUser(List<User> users, int orderQuantity, int iterationQuantity, boolean measureTime) {
        List<Order> orders = List.of();
        if(measureTime) {
            System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + users.size() + "\nMethod: Find all with user\n----------------------");

            for (int i = 0; i < iterationQuantity; i++) {

                long start = System.currentTimeMillis();
                orders = orderJpaRepository.findAllWithUser();
                long finish = System.currentTimeMillis() - start;

                System.out.print((i + 1) + ". " + finish + "ms |");
                DatabaseCleaner.clearDatabase(dataSource);
            }
        }

        return orders;
    }

    public void updateAllStatuses(List<Order> orderList, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: Update all statuses\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);
            saveAll(orderList, 1, false);
            for (int i = 0; i < iterationQuantity; i++) {
                long start = System.currentTimeMillis();
                orderJpaRepository.updateAllStatuses(OrderStatus.COMPLETED);
                long finish = System.currentTimeMillis() - start;
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.UPDATE, orderList.size(), finish, "UPDATE Order o SET o.status = :status");
                System.out.print((i + 1) + ". " + finish + "ms |");
            }
        }
    }

    public void deleteAll(List<Order> orderList, int iterationQuantity, boolean measureTime, boolean batch) {
        if(measureTime) {
            String method = batch ? "deleteAllInBatch()" : "deleteAll()";
            System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + orderList.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);
            for (int i = 0; i < iterationQuantity; i++) {
                saveAll(orderList, 1, false);
                long start = System.currentTimeMillis();
                if(batch) {
                    orderJpaRepository.deleteAllInBatch();
                } else {
                    orderJpaRepository.deleteAll();
                }
                long finish = System.currentTimeMillis() - start;
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.DELETE, orderList.size(), finish, method);
                DatabaseCleaner.clearDatabase((dataSource));
            }
        }
        DatabaseCleaner.clearDatabase((dataSource));
    }
}
