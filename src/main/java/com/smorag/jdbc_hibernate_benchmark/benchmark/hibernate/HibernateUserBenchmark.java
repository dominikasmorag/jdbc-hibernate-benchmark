package com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate;

import com.smorag.jdbc_hibernate_benchmark.BenchmarkData;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import com.smorag.jdbc_hibernate_benchmark.repository.hibernate.UserJpaRepository;
import com.smorag.jdbc_hibernate_benchmark.utils.BenchmarkChartGenerator;
import com.smorag.jdbc_hibernate_benchmark.utils.CsvWriter;
import com.smorag.jdbc_hibernate_benchmark.utils.DatabaseCleaner;
import enums.QueryType;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HibernateUserBenchmark extends HibernateBaseBenchmark{
    private final UserJpaRepository userJpaRepository;
    private final static String ENTITY_NAME = "User";
    private final static String REPORT_FILE_NAME = ENTITY_NAME + DAY + MONTH + ".csv";
    private final CsvWriter csvWriter;

    public HibernateUserBenchmark(UserJpaRepository userJpaRepository, DataSource dataSource) {
        super(dataSource);
        this.userJpaRepository = userJpaRepository;
        this.csvWriter = new CsvWriter(REPORT_FILE_NAME);
    }

    public void saveAll(List<User> userList, int iterationQuantity, boolean measureTime) {

        String method = "saveAll()";

        List<Long> times = new ArrayList<>();

        if (measureTime) {
            System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + userList.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);
        for (int i = 0; i < iterationQuantity ; i++) {
            List<User> copyUsers = BenchmarkData.cloneUsers(userList);
            long start = System.currentTimeMillis();
            userJpaRepository.saveAll(copyUsers);
            long finish = System.currentTimeMillis() - start;
            System.out.print((i + 1) + ". " + finish + "ms |");
            csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.INSERT, userList.size(), finish, method);
            DatabaseCleaner.clearDatabase(dataSource);
            times.add(finish);
        }

            try {
                BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, userList.size(), times);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            } else {
                DatabaseCleaner.clearDatabase(dataSource);
                List<User> copyUsers = BenchmarkData.cloneUsers(userList);
                userJpaRepository.saveAll(copyUsers);
            }
        }

        public List<User> findAll(List<User> userList, int iterationQuantity, boolean measureTime) {
            List<User> users = new ArrayList<>();
            saveAll(userList, 1, false);

            List<Long> times = new ArrayList<>();

            String method = "findAll()";
            if(measureTime) {
                System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + userList.size() + "\nMethod: " + method + "\n----------------------");

                csvWriter.initColumnNames(COLUMN_NAMES);
                for (int i = 0; i < iterationQuantity; i++) {

                long start = System.currentTimeMillis();
                users = userJpaRepository.findAll();
                long finish = System.currentTimeMillis() - start;

                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.SELECT, userList.size(), finish, method);
                times.add(finish);
                }

                try {
                    BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, userList.size(), times);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                DatabaseCleaner.clearDatabase(dataSource);
            }
            else {
                users = userJpaRepository.findAll();
            }
            return users;
        }

        public void deleteAll(List<User> userList, int iterationQuantity, boolean measureTime, boolean batch) {
            if(measureTime) {

                String method = batch ? "deleteAllInBatch()" : "deleteAll()";
                System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + userList.size() + "\nMethod: " + method + "\n----------------------");

                csvWriter.initColumnNames(COLUMN_NAMES);

                List<Long> times = new ArrayList<>();

                for (int i = 0; i < iterationQuantity; i++) {
                    List<User> copyUsers = BenchmarkData.cloneUsers(userList);
                    saveAll(copyUsers, 1, false);
                    long start = System.currentTimeMillis();
                    if(batch) {
                        userJpaRepository.deleteAllInBatch();
                    } else {
                        userJpaRepository.deleteAll();
                    }
                    long finish = System.currentTimeMillis() - start;
                    System.out.print((i + 1) + ". " + finish + "ms |");
                    csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.DELETE, userList.size(), finish, method);
                    times.add(finish);
                }

                try {
                    BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, userList.size(), times);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            DatabaseCleaner.clearDatabase((dataSource));
        }

        public void updateAllFirstNames(List<User> userList, int iterationQuantity, boolean measureTime) {
            String method = "updateAllFirstNames()";

            List<Long> times = new ArrayList<>();

            if(measureTime) {
                System.out.println("=====================\nHIBERNATE\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + userList.size() + "\nMethod: " + method + "\nQuery: UPDATE User u SET u.firstName = :firstName\n----------------------");

                csvWriter.initColumnNames(COLUMN_NAMES);

                saveAll(userList, 1, false);
                for (int i = 0; i < iterationQuantity; i++) {

                    long start = System.currentTimeMillis();
                    userJpaRepository.updateAllFirstNames("Updated_" + i);

                    long finish = System.currentTimeMillis() - start;
                    System.out.print((i + 1) + ". " + finish + "ms |");
                    csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.UPDATE, userList.size(), finish, "UPDATE User u SET u.firstName = :firstName");
                    times.add(finish);
            }

                try {
                    BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, userList.size(), times);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            DatabaseCleaner.clearDatabase(dataSource);
            }
        }

}
