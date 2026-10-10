package com.smorag.jdbc_hibernate_benchmark.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCleaner {

    /**
     * Truncates each of tables
     *
     * @param dataSource datasoure in which database should be cleared
     */
    public static void clearDatabase(DataSource dataSource) {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("TRUNCATE TABLE order_items");
            statement.execute("TRUNCATE TABLE orders");
            statement.execute("TRUNCATE TABLE products");
            statement.execute("TRUNCATE TABLE users");

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
