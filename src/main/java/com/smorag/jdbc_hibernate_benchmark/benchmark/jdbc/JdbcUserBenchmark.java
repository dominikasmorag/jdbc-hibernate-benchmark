package com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc;

import com.smorag.jdbc_hibernate_benchmark.entity.User;
import com.smorag.jdbc_hibernate_benchmark.repository.jdbc.UserJdbcRepository;
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
public class JdbcUserBenchmark extends JdbcBaseBenchmark{

    private final UserJdbcRepository userJdbcRepository;
    private final static String ENTITY_NAME = "User";
    private final static String REPORT_FILE_NAME = ENTITY_NAME + DAY + MONTH + ".csv";
    private final CsvWriter csvWriter;

    public JdbcUserBenchmark(UserJdbcRepository userJdbcRepository, DataSource dataSource) {
        super(dataSource);
        this.userJdbcRepository = userJdbcRepository;
        this.csvWriter = new CsvWriter(REPORT_FILE_NAME);
    }

    public void saveAll(List<User> users, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            String method = "INSERT";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + users.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);

            List<Long> times = new ArrayList<>();

            for (int i = 0; i < iterationQuantity; i++) {
                long start = System.currentTimeMillis();
                userJdbcRepository.saveAll(users);
                long finish = System.currentTimeMillis() - start;
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.INSERT, users.size(), finish, "INSERT INTO users(first_name last_name email) VALUES (? ? ?)");
                System.out.print((i + 1) + ". " + finish + "ms |");
                DatabaseCleaner.clearDatabase(dataSource);
                times.add(finish);
            }

            try {
                BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, users.size(), times);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            userJdbcRepository.saveAll(users);
        }
    }

    public List<User> findAll(List<User> userList, int iterationQuantity, boolean measureTime) {
        List<User> usersFromDb = new ArrayList<>();
        csvWriter.initColumnNames(COLUMN_NAMES);
        saveAll(userList, 1, false);

        List<Long> times = new ArrayList<>();

        if(measureTime) {
            String method = "SELECT";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + userList.size() + "\nMethod: " + method + "\n----------------------");
            for (int i = 0; i < iterationQuantity; i++) {
                long start = System.currentTimeMillis();
                usersFromDb = userJdbcRepository.findAll();
                long finish = System.currentTimeMillis() - start;
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.SELECT, userList.size(), finish, UserJdbcRepository.SELECT_SQL);
                times.add(finish);
            }

            try {
                BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, userList.size(), times);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            DatabaseCleaner.clearDatabase(dataSource);
        }
        return usersFromDb;
    }

    public void deleteUsers(List<User> users, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            String method = "DELETE";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + users.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);

            List<Long> times = new ArrayList<>();

            for (int i = 0; i < iterationQuantity; i++) {
                saveAll(users, 1, false);
                long start = System.currentTimeMillis();
                userJdbcRepository.deleteAll();
                long finish = System.currentTimeMillis() - start;
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.DELETE, users.size(), finish, UserJdbcRepository.DELETE_SQL);
                times.add(finish);
            }

            try {
                BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, users.size(), times);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            DatabaseCleaner.clearDatabase(dataSource);
        }
        }


    public void updateAllFirstNames(List<User> users, int iterationQuantity, boolean measureTime) {
        if(measureTime) {
            String method = "UPDATE";
            System.out.println("=====================\nJDBC\n=====================\nEntity: " + ENTITY_NAME + "\nIteration quantity: " + iterationQuantity + "\nList size: " + users.size() + "\nMethod: " + method + "\n----------------------");
            csvWriter.initColumnNames(COLUMN_NAMES);
            saveAll(users, 1, false);

            List<Long> times = new ArrayList<>();

            for (int i = 0; i < iterationQuantity; i++) {
                long start = System.currentTimeMillis();
                userJdbcRepository.updateAllFirstNames(i + 1);
                long finish = System.currentTimeMillis() - start;
                System.out.print((i + 1) + ". " + finish + "ms |");
                csvWriter.appendRow((i+1), FRAMEWORK, ENTITY_NAME, QueryType.UPDATE, users.size(), finish, UserJdbcRepository.UPDATE_SQL);
                times.add(finish);
            }

            try {
                BenchmarkChartGenerator.generate(FRAMEWORK, ENTITY_NAME, method, users.size(), times);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            DatabaseCleaner.clearDatabase(dataSource);
        }
    }
}
